package com.borachio

import org.scalatest.WordSpec

class MockTest extends WordSpec {
  
  "A mock function" should {
    "return null unless told otherwise" in {
      val m = mockFunction0[String]
      expect(null) { m() }
    }
    
    "return what it's told to return" ignore {
      val m = mockFunction0[Int]
      m.returning(42)
      expect(42) { m() }
    }
  }
}
