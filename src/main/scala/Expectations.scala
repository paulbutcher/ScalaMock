package com.borachio

import scala.collection.mutable.ListBuffer

abstract class Expectations {
  
  private[borachio] def add(expectation: Expectation) {
    handlers += expectation
  }
  
  private[borachio] def verify() {
    handlers.foreach { handler =>
      if (!handler.satisfied)
        throw new ExpectationException("Unsatisfied expectation: "+ handler)
    }
  }
  
  protected val handlers = new ListBuffer[Handler]
}
