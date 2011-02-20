package com.borachio

import scala.collection.mutable.ListBuffer

abstract class Expectations {
  
  private[borachio] def add(expectation: Expectation) {
    expectations += expectation
  }
  
  private[borachio] def handle(mock: MockFunction, arguments: Product): Any
  
  private[borachio] def verify() {
    expectations.foreach { expectation =>
      if (!expectation.satisfied)
        throw new ExpectationException("Put better message here")
    }
  }
  
  protected val expectations = new ListBuffer[Expectation]
}
