package com.borachio

trait MockFactory {
  
  implicit def MockFunctionToExpectation(m: MockFunction) = {
    val expectation = new Expectation(m)
    expectations.add(expectation)
    expectation
  }
  
  def mockFunction[T] = new MockFunction0[T](expectations)
  
  private val expectations = new UnorderedExpectations
}
