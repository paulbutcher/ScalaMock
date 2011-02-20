package com.borachio

import scala.collection.mutable.ListBuffer

abstract class Expectations {
  
  private[borachio] def add(expectation: Expectation) {
    expectations += expectation
  }
  
  private[borachio] def handle(mock: MockFunction): Any
  
  protected val expectations = new ListBuffer[Expectation]
}
