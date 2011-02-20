package com.borachio

import org.scalatest.WordSpec

class MockTest extends WordSpec with MockFactory {
  
  "A mock function" should {
    "return null unless told otherwise" in {
      val m = mockFunction[String]
      m expects ()
      expect(null) { m() }
    }
    
    "return what it's told to" in {
      val m = mockFunction[String]
      m returns "foo"
      expect("foo") { m() }
    }
    
    "match arguments" in {
      val m = mockFunction[Int, String, Double]
      m expects (42, "foo") returning 1.23
      expect(1.23) { m(42, "foo") }
    }
    
    "fail if there are no matching arguments" in {
      val m = mockFunction[Int, String, Double]
      m expects (42, "foo") returning 1.23
      intercept[ExpectationException] { m(42, "bar") }
    }
  }
}
