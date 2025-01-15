package newapi

import org.scalamock.stubs.Stubs
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class CallLogSpec extends AnyFunSpec, Matchers, Stubs:
  trait FirstTrait:
    def foo(x: Int, y: Int): Int
    def foo2(x: Int): Int

  trait SecondTrait:
    def bar(x: String): String

  it("methods log"):
    given log: CallLog = CallLog()
    val first = stub[FirstTrait]
    val second = stub[SecondTrait]

    first.foo.returns(_ => 0)
    first.foo2.returns(_ => 0)
    second.bar.returns(_ => "1")

    first.foo(0, 0)
    second.bar("1")
    first.foo2(2)

    log.toString shouldBe
    """<stub-1> Stub[FirstTrait].foo(x: Int, y: Int)Int
      |<stub-2> Stub[SecondTrait].bar(x: String)String
      |<stub-1> Stub[FirstTrait].foo2(x: Int)Int""".stripMargin


  it("verify"):
    given log: CallLog = CallLog()

    val first = stub[FirstTrait]
    val second = stub[SecondTrait]

    first.foo.returns(_ => 0)
    first.foo2.returns(_ => 0)
    second.bar.returns(_ => "1")

    first.foo(0, 0)
    second.bar("1")
    first.foo2(2)
    first.foo(1, 1)

    second.bar.isBefore(first.foo2) shouldBe true
    second.bar.isAfter(first.foo2) shouldBe false

    first.foo2.isBefore(second.bar) shouldBe false
    first.foo2.isAfter(second.bar) shouldBe true

    first.foo.isBefore(second.bar) shouldBe true
    first.foo.isAfter(second.bar) shouldBe true

    second.bar.isAfter(first.foo) shouldBe true
    second.bar.isBefore(first.foo) shouldBe true

