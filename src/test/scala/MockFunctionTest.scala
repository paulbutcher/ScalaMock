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
  }
}
