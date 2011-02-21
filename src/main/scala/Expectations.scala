package com.borachio

import scala.collection.mutable.ListBuffer

private[borachio] abstract class Expectations {
  
  def add(handler: Handler) {
    handlers += handler
  }
  
  def verify() {
    handlers.foreach { handler =>
      if (!handler.satisfied)
        throw new ExpectationException("Unsatisfied expectation: "+ handler)
    }
  }
  
  def reset() {
    handlers.clear
  }
  
  protected val handlers = new ListBuffer[Handler]
}
