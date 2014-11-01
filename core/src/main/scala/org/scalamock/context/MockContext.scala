package org.scalamock.context

import org.scalamock.handlers.{CallHandler, Handlers}

private[scalamock] trait MockContext {
  type ExpectationException <: Throwable

  private[scalamock] var callLog: CallLog = _
  private[scalamock] var currentExpectationContext: Handlers = _
  private[scalamock] var expectationContext: Handlers = _

  protected def newExpectationException(message: String, methodName: Option[Symbol] = None): ExpectationException

  private[scalamock] def add[E <: CallHandler[_]](e: E) = {
    assert(currentExpectationContext != null, "Null expectation context - missing withExpectations?")
    currentExpectationContext.add(e)
    e
  }

  private[scalamock] def reportUnexpectedCall(call: Call) =
    throw newExpectationException(s"Unexpected call: $call\n\n%s".format(errorContext(callLog, expectationContext)), Some(call.target.name))

  private[scalamock] def reportUnsatisfiedExpectation(callLog: CallLog, expectationContext: Handlers) =
    throw newExpectationException(s"Unsatisfied expectation:\n\n%s".format(errorContext(callLog, expectationContext)))

  private def errorContext(callLog: CallLog, expectationContext: Handlers) =
    s"Expected:\n${expectationContext}\n\nActual:\n${callLog}"
}
