package com.borachio

class UnorderedExpectations extends Expectations {

  private[borachio] def handle(mock: MockFunction, arguments: Product): Any = {
    for (expectation <- expectations) {
      val r = expectation.handle(mock, arguments)
      if (r.isDefined)
        return r.get
    }
    throw new ExpectationException("Put better message here")
  }
  
  private[borachio] def verify() {
    expectations.foreach { expectation =>
      if (!expectation.satisfied)
        throw new ExpectationException("Put better message here")
    }
  }
}
