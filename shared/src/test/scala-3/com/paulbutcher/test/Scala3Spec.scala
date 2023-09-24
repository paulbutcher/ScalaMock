package com.paulbutcher.test

import org.scalamock.scalatest.MockFactory
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class Scala3Spec extends AnyFunSpec with MockFactory with Matchers {


  it("mock traits with parameters") {
    trait Test(val a: Int) {
      def method(x: Int): Int
    }

    val m = mock[Test]
  }

  it("mock parameters with & and | types") {
    trait A
    trait B
    trait Test {
      def method(x: Int | String, y: A & B): Int
    }

    val m = mock[Test]

    (m.method _).expects(*, *).returns(0)
    m.method(1, new A with B) shouldBe 0
  }

  it("mock methods returning function") {
    trait Test {
      def method(x: Int): Int => String
    }

    val m = mock[Test]

    (m.method _).expects(*).returns((_: Int) => "f")
    m.method(1)(0) shouldBe "f"

  }
}
