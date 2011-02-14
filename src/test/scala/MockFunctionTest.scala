package com.borachio

import org.scalatest.WordSpec

class MockTest extends WordSpec with MockFactory {
  
  "A mock function" should {
    "return null unless told otherwise" in {
      val m = mockFunction0[String]
      expect(null) { m() }
    }
    
    "return what it's told to return" ignore {
      val m = mockFunction0[Int]
      m.returns(42)
      expect(42) { m() }
    }
  }
}
