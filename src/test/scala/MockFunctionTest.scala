package com.borachio

import org.scalatest.WordSpec

class MockTest extends WordSpec with MockFactory {
  
  "A mock function" should {
    "return null unless told otherwise" in {
      val m = mockFunction[String]
      expect(null) { m() }
    }
  }
}
