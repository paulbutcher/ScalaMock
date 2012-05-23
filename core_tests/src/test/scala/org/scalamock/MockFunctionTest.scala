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

class MockFunctionTest extends FreeSpec with MockFactory {
  
  autoVerify = false
  
  "Mock functions should" - {
    "have a sensible default name" in {
      val m = mockFunction[String]
      expect("unnamed MockFunction0"){ m.toString }
    }
    
    "have the name we gave them" - {
      "where we use a symbol" in {
        val m1 = mockFunction[String](Symbol("a mock function"))
        expect("a mock function"){ m1.toString }
      }

      "where we use a string" in {
        val m2 = mockFunction[String]("another mock function")
        expect("another mock function"){ m2.toString }
      }
    }
    
    "resolve ambiguity when taking a symbol argument" - {
      "with no name specified" in {
        val m1 = mockFunction[Symbol, String]
        expect("unnamed MockFunction1"){ m1.toString }
      }

      "with a name specified" in {
        val m2 = mockFunction[Symbol, String](functionName("a named mock"))
        expect("a named mock"){ m2.toString }
      }
    }

    "return null by default" in {
      val m = mockFunction[String]
      m.expects()
      expect(null) { m() }
      verifyExpectations
    }
    
    //! TODO - why is this failing?
    "return a null-like default value for non reference types" ignore {
      val m = mockFunction[Int]
      m.expects()
      expect(0) { m() }
      verifyExpectations
    }
    
    "return what they're told to" in {
      val m = mockFunction[String]
      m.expects().returning("a return value")
      expect("a return value") { m() }
      verifyExpectations
    }
    
    "throw what they're told to" in {
      val m = mockFunction[String]
      m.expects().throwing(new RuntimeException("bad thing happened"))
      intercept[RuntimeException]{ m() }
      verifyExpectations
    }
    
    "match literal arguments" in {
      val m = mockFunction[String, Int, Int]
      m.expects("foo", 42)
      m("foo", 42)
      verifyExpectations
    }
    
    "match wildcard arguments" in {
      val m = mockFunction[String, Int, Int]
      m.expects(*, 42)
      m("foo", 42)
      verifyExpectations
    }
    
    "match epsilon arguments" in {
      val m = mockFunction[String, Double, Int]
      m.expects("foo", ~1.0)
      m("foo", 1.0001)
      verifyExpectations
    }
    
    "fail if an expectation is not met" in {
      val m = mockFunction[String, Int, Int]
      m.expects("foo", 42)
      intercept[ExpectationException] { verifyExpectations }
    }
    
    "fail if a method isn't called often enough" in {
      val m = mockFunction[String, Int, Int]
      m.expects("foo", 42).twice
      m("foo", 42)
      intercept[ExpectationException] { verifyExpectations }
    }
    
    "fail if an unexpected call is made" in {
      val m = mockFunction[String, Int, Int]
      intercept[ExpectationException] { m("foo", 42) }
    }
    
    "fail if a method is called too often" in {
      val m = mockFunction[String, Int, Int]
      m.expects("foo", 42).twice
      m("foo", 42)
      m("foo", 42)
      intercept[ExpectationException] { m("foo", 42) }
    }
    
    "match arguments" in {
      val m = mockFunction[Int, Int, String]
      m.expects(where { _ < _ }).returning("less")
      m.expects(where { _ > _ }).returning("more")
      expect("less"){ m(1, 2) }
      expect("more"){ m(2, 1) }
      verifyExpectations
    }
  }
}