package tests

import org.scalamock.stubs.{Stub, ZIOStubs}
import zio.*
import zio.test.*

enum UserStatus:
  case Normal, Blocked

enum FailedAuthResult:
  case UserNotFound, UserNotAllowed, WrongPassword

case class User(id: Long, status: UserStatus)

trait UserService:
  def findUser(userId: Long): UIO[Option[User]]

trait PasswordService:
  def checkPassword(id: Long, password: String): UIO[Boolean]

class UserAuthService(
  userService: UserService,
  passwordService: PasswordService
):
  def authorize(id: Long, password: String): IO[FailedAuthResult, Unit] =
    userService.findUser(id).flatMap:
      case None =>
        ZIO.fail(FailedAuthResult.UserNotFound)

      case Some(user) if user.status == UserStatus.Blocked =>
        ZIO.fail(FailedAuthResult.UserNotAllowed)

      case Some(user) =>
        passwordService.checkPassword(id, password)
          .filterOrFail(identity)(FailedAuthResult.WrongPassword)
          .unit


object ZIOUserAuthServiceSpec extends ZIOSpecDefault, ZIOStubs:

  val unknownUserId = 0
  val user = User(1, UserStatus.Normal)
  val blockedUser = User(2, UserStatus.Blocked)
  val validPassword = "valid"
  val invalidPassword = "invalid"

  case class Verify(
    passwordCheckedTimes: Option[Int]
  )

  val spec =
    suite("UserAuthService")(
      testCase(
        description = "error if user not found",
        id = unknownUserId,
        password = validPassword,
        expectedResult = Exit.fail(FailedAuthResult.UserNotFound),
        verify = Verify(passwordCheckedTimes = Some(0))
      ),
      testCase(
        description = "error if user is blocked",
        id = blockedUser.id,
        password = validPassword,
        expectedResult = Exit.fail(FailedAuthResult.UserNotAllowed),
        verify = Verify(passwordCheckedTimes = Some(0))
      ),
      testCase(
        description = "error if password is invalid",
        id = user.id,
        password = invalidPassword,
        expectedResult = Exit.fail(FailedAuthResult.WrongPassword),
        verify = Verify(passwordCheckedTimes = Some(1))
      ),
      testCase(
        description = "password valid",
        id = user.id,
        password = validPassword,
        expectedResult = Exit.unit,
        verify = Verify(passwordCheckedTimes = Some(1))
      )
    )

  def testCase(
    description: String,
    id: Long,
    password: String,
    expectedResult: Exit[FailedAuthResult, Unit],
    verify: Verify
  ) = test(description) {
    val userService = stub[UserService]
    val passwordService = stub[PasswordService]
    val userAuthService = UserAuthService(userService, passwordService)
    for
      _ <- userService.findUser.returnsZIO:
        case user.id => ZIO.some(user)
        case blockedUser.id => ZIO.some(blockedUser)
        case _ => ZIO.none

      _ <- passwordService.checkPassword.returnsZIO:
        case (_, password) => ZIO.succeed(password == validPassword)

      result <- userAuthService.authorize(id, password).exit

    yield assertTrue(
      result == expectedResult,
      verify.passwordCheckedTimes.contains(passwordService.checkPassword.times)
    )
  }






