package com.borachio

class UnorderedExpectations extends Expectations {

  private[borachio] def handle(mock: MockFunction, arguments: Product): Any = {
    for (handler <- handlers) {
      val r = handler.handle(mock, arguments)
      if (r.isDefined)
        return r.get
    }
    throw new ExpectationException("Unexpected: "+ mock +" with arguments: "+ arguments)
  }
}
