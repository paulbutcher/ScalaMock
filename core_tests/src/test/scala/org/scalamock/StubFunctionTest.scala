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

class StubFunctionTest extends FreeSpec with MockFactory {
  
  autoVerify = false
  
  def repeat(n: Int)(what: => Unit) {
    for (i <- 0 until n)
      what
  }
  
  case class TestException() extends RuntimeException

  "Stub functions should" - {
    "have a sensible default name" in {
      val m = stubFunction[String]
      expect("unnamed StubFunction0"){ m.toString }
    }
    
    "have the name we gave them" - {
      "where we use a symbol" in {
        val m1 = stubFunction[String](Symbol("a stub function"))
        expect("a stub function"){ m1.toString }
      }

      "where we use a string" in {
        val m2 = stubFunction[String]("another stub function")
        expect("another stub function"){ m2.toString }
      }
    }
    
    "resolve ambiguity when taking a symbol argument" - {
      "with no name specified" in {
        val m1 = stubFunction[Symbol, String]
        expect("unnamed StubFunction1"){ m1.toString }
      }

      "with a name specified" in {
        val m2 = stubFunction[Symbol, String](functionName("a named stub"))
        expect("a named stub"){ m2.toString }
      }
    }

    "return null by default" in {
      val m = stubFunction[String]
      expect(null) { m() }
      verifyExpectations
    }
    
    //! TODO - why is this failing?
    "return a null-like default value for non reference types" ignore {
      val m = stubFunction[Int]
      expect(0) { m() }
      verifyExpectations
    }
    
    "return what they're told to" in {
      val m = stubFunction[String]
      m.when().returns("a return value")
      expect("a return value") { m() }
      verifyExpectations
    }
    
    "throw what they're told to" in {
      val m = stubFunction[String]
      m.when().throws(new TestException)
      intercept[TestException]{ m() }
      verifyExpectations
    }
    
    "default to anyNumberOfTimes" in {
      val m = stubFunction[String]
      m.when().returns("a return value")
      expect("a return value") { m() }
      expect("a return value") { m() }
      expect("a return value") { m() }
      verifyExpectations
    }
    
    "unless told otherwise" in {
      val m = stubFunction[String]
      m.when().returns("a return value").twice
      expect("a return value") { m() }
      expect("a return value") { m() }
      expect(null) { m() }
      verifyExpectations
    }
    
    "match literal arguments" in {
      val m = stubFunction[String, Int, Int]
      m("foo", 42)
      m.verify("foo", 42)
      verifyExpectations
    }
    
    "match wildcard arguments" in {
      val m = stubFunction[String, Int, Int]
      m("foo", 42)
      m.verify(*, 42)
      verifyExpectations
    }
    
    "match epsilon arguments" in {
      val m = stubFunction[String, Double, Int]
      m("foo", 1.0001)
      m.verify("foo", ~1.0)
      verifyExpectations
    }

    "fail if an expectation is not met" in {
      val m = stubFunction[String, Int, Int]
      m.verify("foo", 42)
      intercept[ExpectationException] { verifyExpectations }
    }

    "fail if a method isn't called often enough" in {
      val m = stubFunction[String, Int, Int]
      m("foo", 42)
      m.verify("foo", 42).twice
      intercept[ExpectationException] { verifyExpectations }
    }
      
    "fail if a method is called too often" in {
      val m = stubFunction[String, Int, Int]
      m("foo", 42)
      m("foo", 42)
      m("foo", 42)
      m.verify("foo", 42).twice
      intercept[ExpectationException] { verifyExpectations }
    }
    
    "match arguments" - {
      "when stubbing" in {
        val m = stubFunction[Int, Int, String]
        m.when(where { _ < _ }).returns("lower")
        m.when(where { _ > _ }).returns("higher")
        expect("lower"){ m(1, 2) }
        expect("higher"){ m(2, 1) }
        verifyExpectations
      }
      
      "when verifying" in {
        val m = stubFunction[Int, Int, String]
        m(1, 2)
        m(2, 1)
        m(2, 1)
        m.verify(where { _ < _}).once
        m.verify(where { _ > _}).twice
        verifyExpectations
      }
    }
    
    "handle a degenerate sequence" in {
      val m = stubFunction[Int, Int]
      m(42)
      inSequence {
        m.verify(42)
      }
      verifyExpectations
    }
    
    "handle a sequence of calls" in {
      val m = stubFunction[Int, Int]
      repeat(5) { m(42) }
      repeat(1) { m(43) }
      repeat(2) { m(44) }
      inSequence {
        m.verify(42).repeated(3 to 7)
        m.verify(43).once
        m.verify(44).twice
      }
      verifyExpectations
    }
    
    "fail if functions are called out of sequence" in {
      val m = stubFunction[Int, Int]
      repeat(5) { m(42) }
      m(44)
      inSequence {
        m.verify(42).repeated(3 to 7)
        m.verify(43).once
        m.verify(44).twice
      }
      intercept[ExpectationException] { verifyExpectations }
    }
    
    "fail if the entire sequence isn't called" in {
      val m = stubFunction[Int, Int]
      repeat(5) { m(42) }
      repeat(1) { m(43) }
      inSequence {
        m.verify(42).repeated(3 to 7)
        m.verify(43).once
        m.verify(44).twice
      }
      intercept[ExpectationException] { verifyExpectations }
    }

    "handle a combination of ordered and unordered expectations" in {
      val m = stubFunction[Int, Unit]
      
      m(21)
      m(31)
      m(11)
      m(12)
      m(1)
      m(32)
      m(41)
      m(13)

      m.verify(1)
      inSequence {
        m.verify(11)
        m.verify(12)
        m.verify(13)
      }
      m.verify(21)
      inSequence {
        m.verify(31)
        m.verify(32)
      }
      m.verify(41)
      
      verifyExpectations
    }

    "handle a sequence in which functions are called zero times" in {
      val m = stubFunction[Int, Unit]
      m(1)
      m(4)
      inSequence {
        m.verify(1).once
        m.verify(2).never
        m.verify(3).anyNumberOfTimes
        m.verify(4).once
      }
      verifyExpectations
    }

    "handle valid deeply nested expectation contexts" in {
      val m = stubFunction[String, Unit]
      
      m("2.1")
      m("1")
      m("2.2.3")
      m("2.2.2.1")
      m("2.2.2.2")
      m("2.2.1")
      m("3")
      m("2.2.3")
      m("2.3")
      
      m.verify("1")
      inSequence {
        m.verify("2.1")
        inAnyOrder {
          m.verify("2.2.1")
          inSequence {
            m.verify("2.2.2.1")
            m.verify("2.2.2.2")
          }
          m.verify("2.2.3").anyNumberOfTimes
        }
        m.verify("2.3")
      }
      m.verify("3")
      
      verifyExpectations
    }

    "handle invalid deeply nested expectation contexts" in {
      val m = stubFunction[String, Unit]
      
      m("2.1")
      m("1")
      m("2.2.3")
      m("2.2.2.2")

      m.verify("1")
      inSequence {
        m.verify("2.1")
        inAnyOrder {
          m.verify("2.2.1")
          inSequence {
            m.verify("2.2.2.1")
            m.verify("2.2.2.2")
          }
          m.verify("2.2.3")
        }
        m.verify("2.3")
      }
      m.verify("3")
    
      intercept[ExpectationException] { verifyExpectations }
    }
    
    "cope with multiple stubs" in {
      val m1 = stubFunction[Int, String]
      val m2 = stubFunction[Int, String]
      
      m1.when(42).returns("m1")
      m2.when(42).returns("m2")
      
      expect("m1") { m1(42) }
      expect("m2") { m2(42) }
      
      m1.verify(42).once
      m2.verify(42).once
      
      verifyExpectations
    }
  }
}