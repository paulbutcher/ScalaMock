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

class ReturnTest extends IsolatedSpec {

  val intToStringMock = mockFunction[Int, String]
  val intToIntMock = mockFunction[Int, Int]

  behavior of "Mock function"

  it should "return null by default" in {
    intToStringMock.expects(*)
    assertResult(null) { intToStringMock(5) }
  }

  it should "return a null-like default value for non reference types" in {
    intToIntMock.expects(*)
    assertResult(0) { intToIntMock(5) }
  }

  it should "return what they're told to" in {
    intToStringMock.expects(*).returning("a return value")
    assertResult("a return value") { intToStringMock(5) }
  }

  it should "return a calculated return value (chaining mocks)" in {
    val m1 = mockFunction[Int, String]
    val m2 = mockFunction[Int, String]
    m1.expects(42).onCall(m2)
    m2.expects(42).returning("a return value")
    assertResult("a return value") { m1(42) }
  }
}
