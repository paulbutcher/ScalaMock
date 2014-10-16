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

package com.paulbutcher.test.stub

import com.paulbutcher.test._
import org.scalamock.scalatest.MockFactory
import org.scalatest.FreeSpec

class StubTest extends FreeSpec with MockFactory {

  autoVerify = false

  "Stubs should" - {
    "return null unless told otherwise" in {
      withExpectations {
        val m = stub[TestTrait]
        assertResult(null) { m.oneParam(42) }
      }
    }

    "return what they're told to" in {
      withExpectations {
        val m = stub[TestTrait]
        (m.twoParams _).when(42, 1.23).returns("a return value")
        assertResult("a return value") { m.twoParams(42, 1.23) }
      }
    }

    "verify calls" in {
      withExpectations {
        val m = stub[TestTrait]
        m.twoParams(42, 1.23)
        m.twoParams(42, 1.23)
        (m.twoParams _).verify(42, 1.23).twice
      }
    }

    "fail when verification fails" in {
      intercept[ExpectationException](withExpectations {
        val m = stub[TestTrait]
        m.twoParams(42, 1.00)
        (m.twoParams _).verify(42, 1.23).once
      })
    }
  }
}
