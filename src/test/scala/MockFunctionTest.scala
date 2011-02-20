package com.borachio

import org.scalatest.WordSpec

class MockTest extends WordSpec with MockFactory {
  
  autoVerify = false
  
  def repeat(n: Int)(what: => Unit) {
    for (i <- 0 until n)
      what
  }
  
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
      val m1 = mockFunction[Int, String, Double]
      val m2 = mockFunction[String, String]
      m1 expects (42, "foo") returning 1.23
      m2 expects ("foo") returning "bar"
      m1 expects (0, "baz") returning 3.45
      
      expect(3.45) { m1(0, "baz") }
      expect(1.23) { m1(42, "foo") }
      expect("bar") { m2("foo") }
    }
    
    "fail if an expectation is not met" in {
      val m = mockFunction[Int]
      m expects ()
      intercept[ExpectationException] { verifyExpectations }
    }
    
    "allow multiple calls if no range is set" in {
      val m = mockFunction[Int]
      m expects ()
      repeat(3) { m() }
    }
    
    "succeed with the minimum number of calls in a range" in {
      val m = mockFunction[Int]
      m expects () repeat (3 to 7)
      repeat(3) { m() }
      verifyExpectations
    }
    
    "succeed with the maximum number of calls in a range" in {
      val m = mockFunction[Int]
      m expects () repeat (3 to 7)
      repeat(7) { m() }
      verifyExpectations
    }
    
    "fail if the minimum number if calls isn't satisfied" in {
      val m = mockFunction[Int]
      m expects () repeat (3 to 7)
      repeat(2) { m() }
      intercept[ExpectationException] { verifyExpectations }
    }
    
    "fail if the maximum number if calls is exceeded" in {
      val m = mockFunction[Int]
      m expects () repeat (3 to 7)
      intercept[ExpectationException] { repeat(8) { m() } }
    }
  }
}
