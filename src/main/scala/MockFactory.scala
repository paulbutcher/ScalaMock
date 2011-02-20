package com.borachio

import org.scalatest.{BeforeAndAfterEach, Suite}

trait MockFactory extends BeforeAndAfterEach { this: Suite =>
  
  override def beforeEach() {
    expectations = new UnorderedExpectations
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
  
  protected var autoVerify = true
  private var expectations: UnorderedExpectations = _
  private var expectationContext: Expectations = _
}
