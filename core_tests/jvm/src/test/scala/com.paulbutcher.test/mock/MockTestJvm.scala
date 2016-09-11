// Copyright (c) 2011-2015 ScalaMock Contributors (https://github.com/paulbutcher/ScalaMock/graphs/contributors)
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

package com.paulbutcher.test.mock

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FreeSpec, Matchers}
import com.paulbutcher.test._

class MockTestJvm extends FreeSpec with MockFactory with Matchers {
  
  autoVerify = false
  
  "Mocks should" - {
    "cope with infix operators" in {
      withExpectations {
        val m1 = mock[TestTrait]
        val m2 = mock[TestTrait]
        val m3 = mock[TestTrait]
        (m1.+ _).expects(m2).returning(m3)
        assertResult(m3) { m1 + m2 }
      }
    }

    "cope with a var" in {
      withExpectations {
        val m = mock[TestTrait]
        (m.aVar_= _).expects("foo")
        (m.aVar _).expects().returning("bar")
        m.aVar = "foo"
        assertResult("bar") { m.aVar }
      }
    }

    "cope with upper bounds" in {
      withExpectations {
        val m = mock[TestTrait]
        (m.upperBound _).expects((42, "foo")).returning(2)
        assertResult(2) { m.upperBound((42, "foo")) }
      }
    }

    "mock java.io.File" in {
      class MyFile extends java.io.File("")

      withExpectations {
        val m = mock[MyFile]
      }
    }

    "mock a specialized class" in {
      withExpectations {
        val m1 = mock[SpecializedClass[Int]]
        (m1.identity _).expects(42).returning(43)
        assertResult(43) { m1.identity(42) }

        val m2 = mock[SpecializedClass[List[String]]]
        (m2.identity _).expects(List("one", "two", "three")).returning(List("four", "five", "six"))
        assertResult(List("four", "five", "six")) { m2.identity(List("one", "two", "three")) }
      }
    }
  }
}
