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

abstract class ExpectationBase[R](expectedArguments: Product) extends Handler {
  
  def repeat(range: Range) = {
    expectedCalls = range
    this
  }
  
  def repeat(count: Int): ExpectationBase[R] = repeat(count to count)
  
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
  def times() = this

  def returns(value: R) = {
    returnVal = value
    this
  }
  def returning(value: R) = returns(value)

  def handle(call: Call) = {
    if (!isExhausted && expectedArguments == call.arguments) {
      actualCalls += 1
      Some(returnVal)
    } else {
      None
    }
  }
  
  def isSatisfied = expectedCalls contains actualCalls
  
  def isExhausted = expectedCalls.last <= actualCalls
  
  protected var expectedCalls: Range = 1 to 1
  protected var actualCalls: Int = 0
  protected var returnVal: R = _
}
