package com.borachio

import org.scalatest.WordSpec

trait Z {
  def foo(x: Int, y: Int): Int
}

class MockTest extends WordSpec {

  "A mock" should {

    "return null" in {
      val c = new MockContext
      val m = new Mock2[Int, Int, Int](c)
      val z = new Z {
        def foo(x: Int, y: Int) = m(x, y)
      }
      val e = new Expectation2[Int, Int, Int]
      e.returnValue = 42
      e.expectedArguments = Some((1, 3))
      c.setExpectation(e)
      expect(42) { z.foo(1, 2) }
    }
  }
}
