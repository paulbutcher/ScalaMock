package com.borachio

trait MockFactory {
  
  implicit def MockFunctionToExpectation(m: MockFunction) = {
    val expectation = new Expectation(m)
    expectations.add(expectation)
    expectation
  }
  
  def mockFunction[R] = new MockFunction0[R](expectations)
  def mockFunction[T1, R] = new MockFunction1[T1, R](expectations)
  def mockFunction[T1, T2, R] = new MockFunction2[T1, T2, R](expectations)
  
  private val expectations = new UnorderedExpectations
}
