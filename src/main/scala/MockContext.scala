package com.borachio

class MockContext {
  
  def setExpectation(e: Expectation) { expectation = e }
  
  def handle[T1, T2, R](mock: AnyRef, v1: T1, v2: T2): R = {
    val e = expectation.asInstanceOf[Expectation2[T1, T2, R]]
    if (e.expectedArguments.isDefined && e.expectedArguments.get != (v1, v2))
      throw new RuntimeException("Mismatching arguments")
    e.returnValue
  }
  
  private var expectation: Expectation = _
}
