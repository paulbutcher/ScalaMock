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

trait Expectation[R] extends Handler {
  
  def handle(call: Call) = {
    if (expectedArguments == call.arguments) {
      actualCalls += 1
      Some(returnVal)
    } else {
      None
    }
  }
  
  protected var expectedArguments: Option[Product] = None
  protected var expectedCalls: Option[Int] = None
  protected var actualCalls: Int = 0
  protected var returnVal: R = _
}

class Expectation0[R] extends Expectation[R] {
  
  def expects() = this

  def verify() = expects()
}

class Expectation1[T1, R] extends Expectation[R] {
  
  def expects(v1: MockParameter[T1]) = {
    expectedArguments = Some(Tuple1(v1))
    this
  }
  
  def verify(v1: MockParameter[T1]) = expects(v1)
}

class Expectation2[T1, T2, R] extends Expectation[R] {
  
  def expects(v1: MockParameter[T1], v2: MockParameter[T2]) = {
    expectedArguments = Some((v1, v2))
    this
  } 
  
  def verify(v1: MockParameter[T1], v2: MockParameter[T2]) = expects(v1, v2)
}
