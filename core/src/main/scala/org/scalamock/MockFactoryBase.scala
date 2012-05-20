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

import collection.mutable.ListBuffer

trait MockFactoryBase {
  import language.implicitConversions
  
  protected def resetExpectations() {
    callLog.clear
    expectationContext = new UnorderedExpectations
  }
  
  protected def verifyExpectations() {
    callLog foreach expectationContext.handle _
    if (!expectationContext.isSatisfied)
      throw new ExpectationException("Unsatisfied expectation")
    
    expectationContext = null
  }
  
  protected case class MockFunctionName(name: Symbol)
  protected implicit def mockFunctionName(name: Symbol) = MockFunctionName(name)
  protected implicit def mockFunctionName(name: String) = MockFunctionName(Symbol(name))

  protected def mockFunction[R](name: MockFunctionName) = new MockFunction0[R](this, name.name)
  protected def mockFunction[T1, R](name: MockFunctionName) = new MockFunction1[T1, R](this, name.name)
  protected def mockFunction[T1, T2, R](name: MockFunctionName) = new MockFunction2[T1, T2, R](this, name.name)

  protected def mockFunction[R] = new MockFunction0[R](this, Symbol("unnamed MockFunction0"))
  protected def mockFunction[T1, R] = new MockFunction1[T1, R](this, Symbol("unnamed MockFunction1"))
  protected def mockFunction[T1, T2, R] = new MockFunction2[T1, T2, R](this, Symbol("unnamed MockFunction2"))

  protected def stubFunction[R](name: MockFunctionName) = new StubFunction0[R](this, name.name)
  protected def stubFunction[T1, R](name: MockFunctionName) = new StubFunction1[T1, R](this, name.name)
  protected def stubFunction[T1, T2, R](name: MockFunctionName) = new StubFunction2[T1, T2, R](this, name.name)

  protected def stubFunction[R] = new StubFunction0[R](this, Symbol("unnamed StubFunction0"))
  protected def stubFunction[T1, R] = new StubFunction1[T1, R](this, Symbol("unnamed StubFunction1"))
  protected def stubFunction[T1, T2, R] = new StubFunction2[T1, T2, R](this, Symbol("unnamed StubFunction2"))

  protected def * = new MatchAny

  protected class EpsilonMatcher(d: Double) {
    def unary_~() = new MatchEpsilon(d)
  }
  protected implicit def doubleToEpsilon(d: Double) = new EpsilonMatcher(d)

  protected implicit def toMockParameter[T](v: T) = new MockParameter(v)

  protected implicit def MatchAnyToMockParameter[T](m: MatchAny) = new MockParameter[T](m)

  protected implicit def MatchEpsilonToMockParameter[T](m: MatchEpsilon) = new EpsilonMockParameter(m)

  private[scalamock] def logCall(target: FakeFunction, arguments: Product) {
    callLog += Call(target, arguments)
  }
  
  private[scalamock] def add[E <: Expectation[_]](e: E) = {
    expectationContext.add(e)
    e
  }
  
  private val callLog = new ListBuffer[Call]
  private var expectationContext = new UnorderedExpectations
}