package tests

import org.scalamock.stubs.{Stub, ZIOStubs}
import tests.ZIOSpec.{suite, test}
import zio.{IO, Scope, UIO, ZIO}
import zio.test.{Spec, TestEnvironment, ZIOSpecDefault, assertTrue}

object ZIOVerifySpec extends ZIOSpecDefault, ZIOStubs:
  trait FirstTrait:
    def foo(x: Int, y: Int): UIO[Int]

    def foo2(x: Int): UIO[Int]

  trait SecondTrait:
    def bar(x: String): IO[String, String]

  override def spec: Spec[TestEnvironment & Scope, Any] =
    suite("zio verify test-cases")(
      test("verify"):
        given log: CallLog = CallLog()
        val first = stub[FirstTrait]
        val second = stub[SecondTrait]
        for {
          _ <- first.foo.returnsZIO(_ => ZIO.succeed(0))
          _ <- first.foo2.returnsZIO(_ => ZIO.succeed(0))
          _<- second.bar.returnsZIO(_ => ZIO.succeed(""))
          _ <- second.bar("1")
          _ <- first.foo(1, 1)
          _ <- first.foo2(1)
        } yield assertTrue(
          second.bar.isBefore(first.foo),
          second.bar.calls == List("1")
        )
    )