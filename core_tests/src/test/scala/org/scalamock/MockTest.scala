// Copyright (c) 2011-2012 Paul Butcher
// 
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

package org.scalamock

import org.scalamock.scalatest.MockFactory
import org.scalatest.FreeSpec

class MockTest extends FreeSpec with MockFactory {
  
  autoVerify = false
  
  trait TestTrait {
    def nullary: String
    def noParams(): String
    def oneParam(x: Int): String
    def twoParams(x: Int, y: Double): String
//    
//    def overloaded(x: Int): String
//    def overloaded(x: String): String
//    def overloaded(x: Int, y: Double): String
  }
  
  "Mocks should" - {
    "fail if an unexpected method call is made" in {
      val m = mock[TestTrait]
      intercept[ExpectationException] { m.oneParam(42) }
    }
    
    "allow expectations to be set" in {
      val m = mock[TestTrait]
      (m.twoParams _).expects(42, 1.23).returning("a return value")
      expect("a return value") { m.twoParams(42, 1.23) }
      verifyExpectations
    }
    
    "cope with nullary methods" in {
      val m = mock[TestTrait]
      (m.nullary _).expects().returning("a return value")
      expect("a return value") { m.nullary }
      verifyExpectations
    }
    
//    "cope with overloaded methods" in {
//      val m = mock[TestTrait]
//      (m.overloaded _).expects(10).returning("got an integer")
//      expect("got an integer") { m.overloaded(10) }
//      verifyExpectations
//    } 
  }
}