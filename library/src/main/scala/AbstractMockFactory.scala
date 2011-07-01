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

import scala.collection.mutable.Map

trait AbstractMockFactory {
  
  protected var classLoader: Option[MockingClassLoader] = None
  
  protected def resetExpectations() {
    expectations.reset(verbose, callLogging)
    expectationContext = expectations
    mockConstructors.clear
  }

  protected def verifyExpectations() {
    expectations.verify
  }
  
  protected def inSequence(what: => Unit) {
    require(expectationContext == expectations, "inSequence cannot be nested")
    val orderedExpectations = new OrderedExpectations
    expectations.add(orderedExpectations)
    expectationContext = orderedExpectations
    what
    expectationContext = expectations
  }
  
  private[borachio] def add(expectation: Expectation) {
    expectationContext.add(expectation)
  }
  
  private[borachio] def add(mockConstructorMapping: (Class[_], MockConstructor)) {
    mockConstructors += mockConstructorMapping
  }
  
  private[borachio] def findMockConstructor(clazz: Class[_]) = mockConstructors(clazz)
  
  protected def mockFunction[R] = new MockFunction0[R](this, Symbol("unnamed MockFunction0"))
  protected def mockFunction[T1, R] = new MockFunction1[T1, R](this, Symbol("unnamed MockFunction1"))
  protected def mockFunction[T1, T2, R] = new MockFunction2[T1, T2, R](this, Symbol("unnamed MockFunction2"))
  protected def mockFunction[T1, T2, T3, R] = new MockFunction3[T1, T2, T3, R](this, Symbol("unnamed MockFunction3"))
  protected def mockFunction[T1, T2, T3, T4, R] = new MockFunction4[T1, T2, T3, T4, R](this, Symbol("unnamed MockFunction4"))
  protected def mockFunction[T1, T2, T3, T4, T5, R] = new MockFunction5[T1, T2, T3, T4, T5, R](this, Symbol("unnamed MockFunction5"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, R] = new MockFunction6[T1, T2, T3, T4, T5, T6, R](this, Symbol("unnamed MockFunction6"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, R] = new MockFunction7[T1, T2, T3, T4, T5, T6, T7, R](this, Symbol("unnamed MockFunction7"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, R] = new MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](this, Symbol("unnamed MockFunction8"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, R] = new MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](this, Symbol("unnamed MockFunction9"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R] = new MockFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](this, Symbol("unnamed MockFunction10"))
  
  protected def * = new MatchAny
  
  protected class EpsilonMatcher(d: Double) {
    def unary_~() = new MatchEpsilon(d)
  }
  protected implicit def doubleToEpsilon(d: Double) = new EpsilonMatcher(d)
  
  protected implicit def toMockParameter[T](v: T) = new MockParameter(v)
  
  protected implicit def MatchAnyToMockParameter[T](m: MatchAny) = new MockParameter[T](m)
  
  protected implicit def MatchEpsilonToMockParameter[T](m: MatchEpsilon) = new EpsilonMockParameter(m)

  private[borachio] val verbose = false
  private[borachio] val callLogging = false
  private[borachio] val expectations = new UnorderedExpectations

  private var expectationContext: Expectations = _
  private val mockConstructors = Map[Class[_], MockConstructor]()
}
