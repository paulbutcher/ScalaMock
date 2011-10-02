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

import scala.collection.mutable.ListBuffer

trait AbstractMockFactory extends ProxyMockFactory {
  
  protected def resetExpectations() {
    unexpectedCalls.clear
    actualCalls.clear
    expectations = new UnorderedExpectations
    expectationContext = expectations
  }

  protected def verifyExpectations() {
    if (!unexpectedCalls.isEmpty)
      throw new ExpectationException(unexpectedCallsMessage + verboseMessage)

    if (!expectations.satisfied)
      throw new ExpectationException("Unsatisfied expectation: "+ expectations + verboseMessage)
  }
  
  protected def inSequence(what: => Unit) {
    require(expectationContext != null, "Have you remembered to use withExpectations?")
    require(expectationContext == expectations, "inSequence cannot be nested")

    val orderedExpectations = new OrderedExpectations
    expectations.add(orderedExpectations)
    expectationContext = orderedExpectations
    what
    expectationContext = expectations
  }

  protected implicit def MockFunctionToExpectation(m: MockFunction) = {
    require(expectationContext != null, "Have you remembered to use withExpectations?")

    val expectation = new Expectation(m)
    expectationContext.add(expectation)
    expectation
  }
  
  protected def mockFunction[R] = new MockFunction0[R](this)
  protected def mockFunction[T1, R] = new MockFunction1[T1, R](this)
  protected def mockFunction[T1, T2, R] = new MockFunction2[T1, T2, R](this)
  protected def mockFunction[T1, T2, T3, R] = new MockFunction3[T1, T2, T3, R](this)
  protected def mockFunction[T1, T2, T3, T4, R] = new MockFunction4[T1, T2, T3, T4, R](this)
  protected def mockFunction[T1, T2, T3, T4, T5, R] = new MockFunction5[T1, T2, T3, T4, T5, R](this)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, R] = new MockFunction6[T1, T2, T3, T4, T5, T6, R](this)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, R] = new MockFunction7[T1, T2, T3, T4, T5, T6, T7, R](this)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, R] = new MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](this)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, R] = new MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](this)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R] = new MockFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](this)
  
  protected def * = new MatchAny
  
  protected class EpsilonMatcher(d: Double) {
    def unary_~() = new MatchEpsilon(d)
  }
  protected implicit def doubleToEpsilon(d: Double) = new EpsilonMatcher(d)
  
  private[borachio] def handle(mock: MockFunction, arguments: Array[Any]): Any = {
    lazy val description = mock.toString +" with arguments: "+ arguments.mkString("(", ", ", ")")
    val r = expectations.handle(mock, arguments)
    if (r.isDefined) {
      if (callLogging)
        actualCalls += description
      return r.get
    }
    handleUnexpectedCall(description)
  }
  
  private[borachio] def handleUnexpectedCall(description: String) = {
    actualCalls += description
    unexpectedCalls += "Unexpected: "+ description
    throw new ExpectationException(unexpectedCallsMessage + verboseMessage)
  }
  
  private def verboseMessage = (if (verbose) "\n\nExpectations:\n"+ toString else "") + callLog
  
  private def callLog = if (callLogging) "\n\nActual calls:\n"+ actualCallsMessage else ""
  
  private def unexpectedCallsMessage = unexpectedCalls.mkString("\n")

  private def actualCallsMessage = actualCalls.mkString("\n")
  
  private[borachio] val verbose = false
  private[borachio] val callLogging = false
  private[borachio] var expectations = new UnorderedExpectations
  private var expectationContext: Expectations = _

  private val unexpectedCalls = new ListBuffer[String]
  private val actualCalls = new ListBuffer[String]
}
