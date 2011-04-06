// Copyright (c) 2011 Paul Butcher
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

package com.borachio

import com.borachio.scalatest.MockFactory
import org.scalatest.WordSpec

class MockFunctionTest extends WordSpec with MockFactory {
  
  autoVerify = false
  
  def repeat(n: Int)(what: => Unit) {
    for (i <- 0 until n)
      what
  }
  
  case class TestException() extends RuntimeException
  
  "A mock function" should {
    "return null unless told otherwise" in {
      val m = mockFunction[String]
      m expects ()
      expect(null) { m() }
      verifyExpectations
    }
    
    "return what it's told to" in {
      val m = mockFunction[String]
      m returns "foo"
      expect("foo") { m() }
      verifyExpectations
    }
    
    "throw what it's told to" in {
      val m = mockFunction[String]
      m throws new TestException
      intercept[TestException] { m() }
      verifyExpectations
    }
    
    "match arguments" in {
      val m = mockFunction[Int, String, Double]
      m expects (42, "foo") returning 1.23
      expect(1.23) { m(42, "foo") }
      verifyExpectations
    }
    
    "match single element arguments" in {
      val m = mockFunction[Int, Int]
      m expects (42) returning 43
      expect(43) { m(42) }
      verifyExpectations
    }
    
    "fail if there are no matching arguments" in {
      val m = mockFunction[Int, String, Double]
      m expects (42, "foo") returning 1.23
      intercept[ExpectationException] { m(42, "bar") }
    }
    
    "allow any arguments if none are specified" in {
      val m = mockFunction[Int, String, Double]
      m returns 1.23
      expect(1.23) { m(1, "foo") }
      expect(1.23) { m(2, "bar") }
      expect(1.23) { m(-1, null) }
      verifyExpectations
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
      verifyExpectations
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
      verifyExpectations
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
    
    "handle a degenerate sequence" in {
      val m = mockFunction[Int, Int]
      inSequence {
        m expects (42) returning 10
      }
      expect(10) { m(42) }
      verifyExpectations
    }
    
    "handle a sequence of calls" in {
      val m = mockFunction[Int, Int]
      inSequence {
        m expects (42) returning 10 repeat (3 to 7)
        m expects (43) returning 11 repeat 1
        m expects (44) returning 12 twice
      }
      repeat(5) { expect(10) { m(42) } }
      repeat(1) { expect(11) { m(43) } }
      repeat(2) { expect(12) { m(44) } }
      verifyExpectations
    }
    
    "fail if functions are called out of sequence" in {
      val m = mockFunction[Int, Int]
      inSequence {
        m expects (42) returning 10 repeat (3 to 7)
        m expects (43) returning 11 repeat 1
        m expects (44) returning 12 twice
      }
      repeat(5) { m(42) }
      intercept[ExpectationException] { m(44) }
    }
    
    "fail if the entire sequence isn't called" in {
      val m = mockFunction[Int, Int]
      inSequence {
        m expects (42) returning 10 repeat (3 to 7)
        m expects (43) returning 11 once;
        m expects (44) returning 12 twice
      }
      repeat(5) { m(42) }
      repeat(1) { m(43) }
      intercept[ExpectationException] { verifyExpectations }
    }
    
    "handle a combination of ordered and unordered expectations" in {
      val m = mockFunction[Int, Unit]

      m expects (1)
      inSequence {
        m expects (11)
        m expects (12)
        m expects (13)
      }
      m expects (21)
      inSequence {
        m expects (31)
        m expects (32)
      }
      m expects (41)
      
      m(21)
      m(31)
      m(11)
      m(12)
      m(1)
      m(32)
      m(41)
      m(13)
      
      verifyExpectations
    }
    
    "handle a sequence in which functions are called zero times" in {
      val m = mockFunction[Int, Unit]
      inSequence {
        m expects (1) once;
        m expects (2) never;
        m expects (3) anyNumberOfTimes;
        m expects (4) once
      }
      m(1)
      m(4)
      verifyExpectations
    }
    
    "match wildcard arguments" in {
      val m = mockFunction[Int, String, Unit]
      m expects (42, "foo")
      m expects (*, "bar")
      m expects (0, *)
      
      m(42, "foo")
      m(1, "bar")
      m(2, "bar")
      m(0, "something")
      m(0, null)
      intercept[ExpectationException] { m(1, "something") }
    }
    
    "match epsilon arguments" in {
      val m = mockFunction[Double, Double]
      m expects (~42.0) returning 1.0
      m expects (~0.0)
      
      m(42.0001)
      m(-0.0001)
      intercept[ExpectationException] { m(42.1) }
    }
  }
}
