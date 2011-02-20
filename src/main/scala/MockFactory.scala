package com.borachio

import org.scalatest.{BeforeAndAfterEach, Suite}

trait MockFactory extends BeforeAndAfterEach { this: Suite =>
  
  override def beforeEach() {
    expectations = new UnorderedExpectations
  }
  
  override def afterEach() {
    if (autoVerify)
      verifyExpectations
  }

  protected def verifyExpectations() {
    //! TODO
  }

  implicit def MockFunctionToExpectation(m: MockFunction) = {
    val expectation = new Expectation(m)
    expectations.add(expectation)
    expectation
  }
  
  def mockFunction[R] = new MockFunction0[R](expectations)
  def mockFunction[T1, R] = new MockFunction1[T1, R](expectations)
  def mockFunction[T1, T2, R] = new MockFunction2[T1, T2, R](expectations)
  
  protected var autoVerify = true
  private var expectations = new UnorderedExpectations
}
