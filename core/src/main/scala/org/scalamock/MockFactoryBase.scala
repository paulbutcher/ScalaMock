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

trait MockFactoryBase {
  import language.implicitConversions
  
  protected case class MockFunctionName(name: Symbol)
  protected implicit def mockFunctionName(name: Symbol) = MockFunctionName(name)
  protected implicit def mockFunctionName(name: String) = MockFunctionName(Symbol(name))

  protected def mockFunction[R](name: MockFunctionName) = new MockFunction0[R](this, name.name)
  protected def mockFunction[T1, R](name: MockFunctionName) = new MockFunction1[T1, R](this, name.name)
  protected def mockFunction[T1, T2, R](name: MockFunctionName) = new MockFunction2[T1, T2, R](this, name.name)

  protected def mockFunction[R] = new MockFunction0[R](this, Symbol("unnamed MockFunction0"))
  protected def mockFunction[T1, R] = new MockFunction1[T1, R](this, Symbol("unnamed MockFunction1"))
  protected def mockFunction[T1, T2, R] = new MockFunction2[T1, T2, R](this, Symbol("unnamed MockFunction2"))
  
  protected implicit def toExpectation[R](m: MockFunction0[R]) = new Expectation0[R]
  protected implicit def toExpectation[T1, R](m: MockFunction1[T1, R]) = new Expectation1[T1, R]
  protected implicit def toExpectation[T1, T2, R](m: MockFunction2[T1, T2, R]) = new Expectation2[T1, T2, R]
  
  protected implicit def toMockParameter[T](v: T) = new MockParameter(v)
  
  private[scalamock] def logCall(target: Handler, arguments: Product) = callLog.log(target, arguments)
  
  private val callLog = new CallLog
}