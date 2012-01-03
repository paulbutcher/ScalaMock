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

package org.scalamock

import scala.collection.mutable.ListBuffer

trait MockFactoryBase {
  
  protected def resetExpectations() {
    unexpectedCalls.clear
    actualCalls.clear
    expectationContext = new UnorderedExpectations
  }

  protected def verifyExpectations() {
    if (!unexpectedCalls.isEmpty)
      throw new ExpectationException(unexpectedCallsMessage + verboseMessage)

    if (!expectationContext.satisfied)
      throw new ExpectationException("Unsatisfied expectation: "+ expectationContext.unsatisfiedString + verboseMessage)
      
    expectationContext = null
  }
  
  protected def resetMocks() {}
  
  protected def inAnyOrder(what: => Unit) {
    inContext(new UnorderedExpectations)(what)
  }
  
  protected def inSequence(what: => Unit) {
    inContext(new OrderedExpectations)(what)
  }
  
  private[scalamock] def add[T <: Expectation](expectation: T) = {
    require(expectationContext != null, "Have you remembered to use withExpectations?")
    
    expectationContext.add(expectation)
    expectation
  }
  
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
  
  implicit def mockFunction0ToExpectation[R](m: MockFunction0[R]) = m.toTypeSafeExpectation0[R]
  implicit def mockFunction1ToExpectation[T1, R](m: MockFunction1[T1, R]) = m.toTypeSafeExpectation1[T1, R]
  implicit def mockFunction2ToExpectation[T1, T2, R](m: MockFunction2[T1, T2, R]) = m.toTypeSafeExpectation2[T1, T2, R]
  implicit def mockFunction3ToExpectation[T1, T2, T3, R](m: MockFunction3[T1, T2, T3, R]) = m.toTypeSafeExpectation3[T1, T2, T3, R]
  implicit def mockFunction4ToExpectation[T1, T2, T3, T4, R](m: MockFunction4[T1, T2, T3, T4, R]) = m.toTypeSafeExpectation4[T1, T2, T3, T4, R]
  implicit def mockFunction5ToExpectation[T1, T2, T3, T4, T5, R](m: MockFunction5[T1, T2, T3, T4, T5, R]) = m.toTypeSafeExpectation5[T1, T2, T3, T4, T5, R]
  implicit def mockFunction6ToExpectation[T1, T2, T3, T4, T5, T6, R](m: MockFunction6[T1, T2, T3, T4, T5, T6, R]) = m.toTypeSafeExpectation6[T1, T2, T3, T4, T5, T6, R]
  implicit def mockFunction7ToExpectation[T1, T2, T3, T4, T5, T6, T7, R](m: MockFunction7[T1, T2, T3, T4, T5, T6, T7, R]) = m.toTypeSafeExpectation7[T1, T2, T3, T4, T5, T6, T7, R]
  implicit def mockFunction8ToExpectation[T1, T2, T3, T4, T5, T6, T7, T8, R](m: MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R]) = m.toTypeSafeExpectation8[T1, T2, T3, T4, T5, T6, T7, T8, R]
  implicit def mockFunction9ToExpectation[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](m: MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]) = m.toTypeSafeExpectation9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]
  implicit def mockFunction10ToExpectation[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](m: MockFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R]) = m.toTypeSafeExpectation10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R]
  
  protected def where[T1](matcher: T1 => Boolean) = new MockMatcher1(matcher)
  protected def where[T1, T2](matcher: (T1, T2) => Boolean) = new MockMatcher2(matcher)
  protected def where[T1, T2, T3](matcher: (T1, T2, T3) => Boolean) = new MockMatcher3(matcher)
  protected def where[T1, T2, T3, T4](matcher: (T1, T2, T3, T4) => Boolean) = new MockMatcher4(matcher)
  protected def where[T1, T2, T3, T4, T5](matcher: (T1, T2, T3, T4, T5) => Boolean) = new MockMatcher5(matcher)
  protected def where[T1, T2, T3, T4, T5, T6](matcher: (T1, T2, T3, T4, T5, T6) => Boolean) = new MockMatcher6(matcher)
  protected def where[T1, T2, T3, T4, T5, T6, T7](matcher: (T1, T2, T3, T4, T5, T6, T7) => Boolean) = new MockMatcher7(matcher)
  protected def where[T1, T2, T3, T4, T5, T6, T7, T8](matcher: (T1, T2, T3, T4, T5, T6, T7, T8) => Boolean) = new MockMatcher8(matcher)
  protected def where[T1, T2, T3, T4, T5, T6, T7, T8, T9](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => Boolean) = new MockMatcher9(matcher)
  protected def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => Boolean) = new MockMatcher10(matcher)
  
  protected def * = new MatchAny
  
  protected implicit def wildcard[T] = new MockParameter[T](*)
  
  protected class EpsilonMatcher(d: Double) {
    def unary_~() = new MatchEpsilon(d)
  }
  protected implicit def doubleToEpsilon(d: Double) = new EpsilonMatcher(d)
  
  protected def **(values: MockParameter[_]*) = new MatchRepeated(values: _*)
  
  protected implicit def toMockParameter[T](v: T) = new MockParameter(v)
  
  protected implicit def MatchAnyToMockParameter[T](m: MatchAny) = new MockParameter[T](m)
  
  protected implicit def MatchEpsilonToMockParameter[T](m: MatchEpsilon) = new EpsilonMockParameter(m)

  private[scalamock] def handle(mock: MockFunction, arguments: Array[Any]): Any = {
    lazy val description = mock.toString +" with arguments: "+ arguments.mkString("(", ", ", ")")
    val r = expectationContext.handle(mock, arguments)
    if (r.isDefined) {
      if (callLogging)
        actualCalls += description
      return r.get
    }
    if (mock.failIfUnexpected)
      handleUnexpectedCall(description)
    else
      null
  }
  
  private[scalamock] def handleUnexpectedCall(description: String) = {
    actualCalls += description
    unexpectedCalls += "Unexpected: "+ description
    throw new ExpectationException(unexpectedCallsMessage + verboseMessage)
  }
  
  private def verboseMessage = (if (verbose) "\n\nExpectations:\n"+ expectationContext else "") + callLog
  
  private def callLog = if (callLogging) "\n\nActual calls:\n"+ actualCallsMessage else ""
  
  private def unexpectedCallsMessage = unexpectedCalls.mkString("\n")

  private def actualCallsMessage = actualCalls.mkString("\n")
  
  private def inContext(context: Expectations)(what: => Unit) {
    require(expectationContext != null, "Have you remembered to use withExpectations?")

    expectationContext.add(context)
    val prevContext = expectationContext
    expectationContext = context
    what
    expectationContext = prevContext
  }
  
  private[scalamock] val verbose = false
  private[scalamock] val callLogging = false
  private var expectationContext: Expectations = _

  private val unexpectedCalls = new ListBuffer[String]
  private val actualCalls = new ListBuffer[String]
}
