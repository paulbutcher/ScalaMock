package com.borachio

class UnorderedExpectations extends Expectations {

  private[borachio] def handle(mock: MockFunction) = {
    expectations.find { _.canHandle(mock) } match {
      case Some(expectation) => expectation.handle
      case None => throw new ExpectationException("Put better message here")
    }
  }
}
