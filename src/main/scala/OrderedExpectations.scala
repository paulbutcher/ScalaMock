package com.borachio

private[borachio] class OrderedExpectations extends Expectations with Handler {

  private[borachio] def handle(mock: MockFunction, arguments: Product): Option[Any] = {
    for (i <- currentIndex until handlers.length) {
      val handler = handlers(i)
      val r = handler.handle(mock, arguments)
      if (r.isDefined) {
        currentIndex = i
        return r
      }
      if (!handler.satisfied)
        return None
    }
    None
  }
  
  private[borachio] def satisfied = handlers.forall { _.satisfied }

  var currentIndex = 0
}
