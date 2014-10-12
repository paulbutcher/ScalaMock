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

package com.paulbutcher.test

import org.scalamock.scalatest.MockFactory
import org.scalatest.FreeSpec

class CallCountTest extends FreeSpec with MockFactory {
  
  autoVerify = false
  
  def repeat(n: Int)(what: => Unit) {
    for (i <- 0 until n)
      what
  }
  
  "Mock functions should" - {

    "fail if a method isn't called often enough" in {
      intercept[ExpectationException](withExpectations {
        val m = mockFunction[String, Int, Int]
        m.expects("foo", 42).twice
        m("foo", 42)
      })
    }
    
    "fail if an unexpected call is made" in {
      withExpectations {
        val m = mockFunction[String, Int, Int]
        intercept[ExpectationException] { m("foo", 42) }
      }
    }
    
    "fail if a method is called too often" in {
      withExpectations {
        val m = mockFunction[String, Int, Int]
        m.expects("foo", 42).twice
        m("foo", 42)
        m("foo", 42)
        intercept[ExpectationException] { m("foo", 42) }
      }
    }
    
    "treat stubs as syntactic sugar for anyNumberOfTimes" in {
      withExpectations {
        val m = mockFunction[Int, String]
        
        m.stubs(*).returning("a return value")
        
        assertResult("a return value") { m(1) }
        assertResult("a return value") { m(2) }
        assertResult("a return value") { m(3) }
      }
    }
  }
}
