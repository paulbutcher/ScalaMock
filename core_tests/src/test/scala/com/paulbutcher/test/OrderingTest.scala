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

class OrderingTest extends IsolatedSpec {

  autoVerify = false

  behavior of "Mock function"

  it should "handle a degenerate sequence" in withExpectations {
    val m = mockFunction[Int, Int]
    inSequence {
      m.expects(42).returning(10)
    }
    assertResult(10) { m(42) }
  }

  it should "handle a sequence of calls" in withExpectations {
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

  it should "fail if functions are called out of sequence" in {
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

  it should "fail if the entire sequence isn't called" in {
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

  it should "not match a previous item in the sequence" in withExpectations {
    val m = mockFunction[Int, Int]
    inSequence {
      m.expects(42).returning(10).anyNumberOfTimes
      m.expects(43).returning(11).once
    }
    assertResult(10) { m(42) }
    assertResult(11) { m(43) }
    intercept[ExpectationException] { m(42) }
  }

  it should "handle a combination of ordered and unordered expectations" in withExpectations {
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

  it should "handle a sequence in which functions are called zero times" in withExpectations {
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

  it should "handle valid deeply nested expectation contexts" in withExpectations {
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

  it should "handle invalid deeply nested expectation contexts" in {
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
}
