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

class CallHandler[R](private[scalamock] val argumentMatcher: Product => Boolean) extends Handler {
  
  def repeat(range: Range) = {
    expectedCalls = range
    this
  }
  
  def repeat(count: Int): CallHandler[R] = repeat(count to count)
  
  def never() = repeat(0)
  def once() = repeat(1)
  def twice() = repeat(2)
  
  def anyNumberOfTimes() = repeat(0 to scala.Int.MaxValue - 1)
  def atLeastOnce() = repeat(1 to scala.Int.MaxValue - 1)
  def atLeastTwice() = repeat(2 to scala.Int.MaxValue - 1)

  def noMoreThanOnce() = repeat(0 to 1)
  def noMoreThanTwice() = repeat(0 to 2)
  
  def repeated(range: Range) = repeat(range)
  def repeated(count: Int) = repeat(count)
  def times() = CallHandler.this

  def returns(value: R) = {
    onCallHandler = {_ => value}
    this
  }
  def returning(value: R) = returns(value)
  
  def throws(e: Throwable) = {
    onCallHandler = {_ => throw e}
    this
  }
  def throwing(e: Throwable) = throws(e)

  def handle(call: Call) = {
    if (!isExhausted && argumentMatcher(call.arguments)) {
      actualCalls += 1
      Some(onCallHandler(call.arguments))
    } else {
      None
    }
  }
  
  def verify(call: Call) = false
  
  def isSatisfied = expectedCalls contains actualCalls
  
  def isExhausted = expectedCalls.last <= actualCalls
  
  private[scalamock] var expectedCalls: Range = 1 to 1
  private[scalamock] var actualCalls: Int = 0
  private[scalamock] var onCallHandler: Product => R = {_ => null.asInstanceOf[R]}
}

trait Verify { self: CallHandler[_] =>
  
  override def handle(call: Call) = sys.error("verify should appear after all code under test has been exercised")
  
  override def verify(call: Call) = {
    if (argumentMatcher(call.arguments)) {
      actualCalls += 1
      true
    } else {
      false
    }
  }
}

class CallHandler0[R] extends CallHandler[R](new FunctionAdapter0({() => true}))

class CallHandler1[T1, R](v1: MockParameter[T1]) extends CallHandler[R](new FunctionAdapter1({p1: T1 => v1 == p1}))

class CallHandler2[T1, T2, R](v1: MockParameter[T1], v2: MockParameter[T2]) extends CallHandler[R](new FunctionAdapter2({(p1: T1, p2: T2) => v1 == p1 && v2 == p2}))
