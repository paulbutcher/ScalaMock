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
import org.scalamock._

class MockFunctionTest extends FreeSpec with MockFactory {
  
  autoVerify = false
  
  def repeat(n: Int)(what: => Unit) {
    for (i <- 0 until n)
      what
  }
  
  case class TestException() extends RuntimeException

  "Mock functions should" - {
    "have a sensible default name" in {
      val m = mockFunction[String]
      assertResult("unnamed MockFunction0"){ m.toString }
    }
    
    "have the name we gave them" - {
      "where we use a symbol" in {
        val m1 = mockFunction[String](Symbol("a mock function"))
        assertResult("a mock function"){ m1.toString }
      }

      "where we use a string" in {
        val m2 = mockFunction[String]("another mock function")
        assertResult("another mock function"){ m2.toString }
      }
    }
    
    "resolve ambiguity when taking a symbol argument" - {
      "with no name specified" in {
        val m1 = mockFunction[Symbol, String]
        assertResult("unnamed MockFunction1"){ m1.toString }
      }

      "with a name specified" in {
        val m2 = mockFunction[Symbol, String](functionName("a named mock"))
        assertResult("a named mock"){ m2.toString }
      }
    }

    "return null by default" in {
      withExpectations {
        val m = mockFunction[String]
        m.expects()
        assertResult(null) { m() }
      }
    }
    
    "return a null-like default value for non reference types" in {
      withExpectations {
        val m = mockFunction[Int]
        m.expects()
        assertResult(0) { m() }
      }
    }
    
    "return what they're told to" in {
      withExpectations {
        val m = mockFunction[String]
        m.expects().returning("a return value")
        assertResult("a return value") { m() }
      }
    }
    
    "throw what they're told to" in {
      withExpectations {
        val m = mockFunction[String]
        m.expects().throwing(new TestException)
        intercept[TestException]{ m() }
      }
    }
    
    "return a calculated return value" in {
      withExpectations {
        val m1 = mockFunction[Int, String]
        val m2 = mockFunction[Int, String]
        m1.expects(42).onCall(m2)
        m2.expects(42).returning("a return value")
        assertResult("a return value") { m1(42) }
      }
    }
    
    "match literal arguments" in {
      withExpectations {
        val m = mockFunction[String, Int, Int]
        m.expects("foo", 42)
        m("foo", 42)
      }
    }
    
    "match wildcard arguments" in {
      withExpectations {
        val m = mockFunction[String, Int, Int]
        m.expects(*, 42)
        m("foo", 42)
      }
    }
    
    "match epsilon arguments" in {
      withExpectations {
        val m = mockFunction[String, Double, Int]
        m.expects("foo", ~1.0)
        m("foo", 1.0001)
      }
    }
    
    "fail if an expectation is not met" in {
      intercept[ExpectationException](withExpectations {
        val m = mockFunction[String, Int, Int]
        m.expects("foo", 42)
      })
    }
    
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
    
    "match arguments" in {
      withExpectations {
        val m = mockFunction[Int, Int, String]
        m.expects(where { _ < _ }).returning("less")
        m.expects(where { _ > _ }).returning("more")
        assertResult("less"){ m(1, 2) }
        assertResult("more"){ m(2, 1) }
      }
    }
    
    "handle a degenerate sequence" in {
      withExpectations {
        val m = mockFunction[Int, Int]
        inSequence {
          m.expects(42).returning(10)
        }
        assertResult(10) { m(42) }
      }
    }
    
    "handle a sequence of calls" in {
      withExpectations {
        val m = mockFunction[Int, Int]
        inSequence {
          m.expects(42).returning(10).repeated(3 to 7)
          m.expects(43).returning(11).once
          m.expects(44).returning(12).twice
        }
        repeat(5) { assertResult(10) { m(42) } }
        repeat(1) { assertResult(11) { m(43) } }
        repeat(2) { assertResult(12) { m(44) } }
      }
    }
    
    "fail if functions are called out of sequence" in {
      intercept[ExpectationException](withExpectations {
        val m = mockFunction[Int, Int]
        inSequence {
          m.expects(42).returning(10).repeated(3 to 7)
          m.expects(43).returning(11).once
          m.expects(44).returning(12).twice
        }
        repeat(5) { m(42) }
      })
    }
    
    "fail if the entire sequence isn't called" in {
      intercept[ExpectationException](withExpectations {
        val m = mockFunction[Int, Int]
        inSequence {
          m.expects(42).returning(10).repeated(3 to 7)
          m.expects(43).returning(11).once
          m.expects(44).returning(12).twice
        }
        repeat(5) { assertResult(10) { m(42) } }
        repeat(1) { assertResult(11) { m(43) } }
      })
    }
    
    "not match a previous item in the sequence" in {
      withExpectations {
        val m = mockFunction[Int, Int]
        inSequence {
          m.expects(42).returning(10).anyNumberOfTimes
          m.expects(43).returning(11).once
        }
        assertResult(10) { m(42) }
        assertResult(11) { m(43) }
        intercept[ExpectationException] { m(42) }
      }
    }
    
    "handle a combination of ordered and unordered expectations" in {
      withExpectations {
        val m = mockFunction[Int, Unit]
  
        m.expects(1)
        inSequence {
          m.expects(11)
          m.expects(12)
          m.expects(13)
        }
        m.expects(21)
        inSequence {
          m.expects(31)
          m.expects(32)
        }
        m.expects(41)
        
        m(21)
        m(31)
        m(11)
        m(12)
        m(1)
        m(32)
        m(41)
        m(13)
      }
    }
    
    "handle a sequence in which functions are called zero times" in {
      withExpectations {
        val m = mockFunction[Int, Unit]
        inSequence {
          m.expects(1).once
          m.expects(2).never
          m.expects(3).anyNumberOfTimes
          m.expects(4).once
        }
        m(1)
        m(4)
      }
    }

    "handle valid deeply nested expectation contexts" in {
      withExpectations {
        val m = mockFunction[String, Unit]
        
        m.expects("1")
        inSequence {
          m.expects("2.1")
          inAnyOrder {
            m.expects("2.2.1")
            inSequence {
              m.expects("2.2.2.1")
              m.expects("2.2.2.2")
            }
            m.expects("2.2.3").anyNumberOfTimes
          }
          m.expects("2.3")
        }
        m.expects("3")
        
        m("2.1")
        m("1")
        m("2.2.3")
        m("2.2.2.1")
        m("2.2.2.2")
        m("2.2.1")
        m("3")
        m("2.2.3")
        m("2.3")
      }
    }
    
    "handle invalid deeply nested expectation contexts" in {
      intercept[ExpectationException](withExpectations {
        val m = mockFunction[String, Unit]
        
        m.expects("1")
        inSequence {
          m.expects("2.1")
          inAnyOrder {
            m.expects("2.2.1")
            inSequence {
              m.expects("2.2.2.1")
              m.expects("2.2.2.2")
            }
            m.expects("2.2.3")
          }
          m.expects("2.3")
        }
        m.expects("3")
        
        m("2.1")
        m("1")
        m("2.2.3")
      })
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
