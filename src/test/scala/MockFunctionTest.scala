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
    
    "match single element arguments" in {
      val m = mockFunction[Int, Int]
      m expects (42) returning 43
      expect(43) { m(42) }
    }
    
    "fail if there are no matching arguments" in {
      val m = mockFunction[Int, String, Double]
      m expects (42, "foo") returning 1.23
      intercept[ExpectationException] { m(42, "bar") }
    }
    
    "match multiple expectations in any order" in {
      val m = mockFunction[Int, String, Double]
      m expects (42, "foo") returning 1.23
      m expects (10, "bar") returning 2.34
      m expects (0, "baz") returning 3.45
      
      expect(3.45) { m(0, "baz") }
      expect(1.23) { m(42, "foo") }
      expect(2.34) { m(10, "bar") }
    }
  }
}
