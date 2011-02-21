package com.borachio

import org.scalatest.{BeforeAndAfterEach, Suite}

trait MockFactory extends BeforeAndAfterEach { this: Suite =>
  
  override def beforeEach() {
    expectations.reset
    expectationContext = expectations
  }
  
  override def afterEach() {
    if (autoVerify)
      verifyExpectations
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

  protected implicit def MockFunctionToExpectation(m: MockFunction) = {
    val expectation = new Expectation(m)
    expectationContext.add(expectation)
    expectation
  }
  
  protected def mockFunction[R] = new MockFunction0[R](expectations)
  protected def mockFunction[T1, R] = new MockFunction1[T1, R](expectations)
  protected def mockFunction[T1, T2, R] = new MockFunction2[T1, T2, R](expectations)
  protected def mockFunction[T1, T2, T3, R] = new MockFunction3[T1, T2, T3, R](expectations)
  protected def mockFunction[T1, T2, T3, T4, R] = new MockFunction4[T1, T2, T3, T4, R](expectations)
  protected def mockFunction[T1, T2, T3, T4, T5, R] = new MockFunction5[T1, T2, T3, T4, T5, R](expectations)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, R] = new MockFunction6[T1, T2, T3, T4, T5, T6, R](expectations)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, R] = new MockFunction7[T1, T2, T3, T4, T5, T6, T7, R](expectations)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, R] = new MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](expectations)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, R] = new MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](expectations)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R] = new MockFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](expectations)
  
  protected var autoVerify = true
  private val expectations = new UnorderedExpectations
  private var expectationContext: Expectations = _
}
