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

class ErrorMessageTest extends WordSpec with MockFactory with VerboseErrors with CallLogging {

  "A mock function" when {
    "called unexpectedly" should {
      "generate a sensible error message" ignore {
        val m = mockFunction[Int, String, Double]
        m(42, "foo")
      }
    }
    
    "unsatisfied" should {
      "generate a sensible error message" ignore {
        val m = mockFunction[Int, String, Double]
        m expects (42, "foo") returning 1.0
      }
    }
  }

  "A proxy mock" when {
    "called unexpectedly" should {
      "generate a sensible error message" ignore {
        val m = mock[Seq[String]]
        m.indexOf("foo", 10)
      }
    }
    
    "unsatisfied" should {
      "generate a sensible error message" ignore {
        val m = mock[Seq[String]]
        m expects 'indexOf withArgs ("foo", 10) returning 42
      }
    }
  }
  
  "A sequence of expectations" when {
    "unsatisfied" should {
      "generate a sensible error message" ignore {
        val m = mockFunction[Int, String, Double]
        inSequence {
          m expects (42, "foo")
          m expects (1, "bar")
        }
        m(42, "foo")
      }
    }
  }
  
  "A complicated sequence of expectations" should {
    "generate a sensible error message" ignore {
      val m1 = mockFunction[Int, String, Double]
      val m2 = mock[Seq[String]]
      
      m1 expects (42, "foo") returning 1.0 atLeastTwice;
      inSequence {
        m2 expects 'indexOf withArgs ("foo", 10) returning 42 once;
        m1 expects (3, "bar") returning 1.23
      }
      m2 expects 'head throwing (new NoSuchElementException)
      
      m2.indexOf("foo", 10)
      m2.isEmpty
    }
  }
  
  "A SUT that swallows exceptions" should {
    "generate a sensible error message" ignore {
      val m = mockFunction[Int, Unit]
      m expects (1)
      
      try {
        m(1)
        m(2)
      } catch {
        case _ => m(3)
      }
    }
  }
}
