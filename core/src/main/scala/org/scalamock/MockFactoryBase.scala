// 
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

package org.scalamock

import org.scalamock.clazz.Mock
import org.scalamock.context.{ Call, CallLog }
import org.scalamock.function._
import org.scalamock.handlers.{ CallHandler, Handlers, OrderedHandlers, UnorderedHandlers }
import org.scalamock.matchers._

trait MockFactoryBase extends Mock with MockFunctions with Matchers {
  import scala.language.implicitConversions

  type ExpectationException <: Throwable

  private[scalamock] var callLog: CallLog = _
  private[scalamock] var currentExpectationContext: Handlers = _
  private[scalamock] var expectationContext: Handlers = _

  initializeExpectations

  def withExpectations[T](what: => T): T = {
    if (expectationContext == null) {
      // we don't reset expectations for the first test case to allow
      // defining expectations in Suite scope and writing tests in OneInstancePerTest/isolated style 
      initializeExpectations
    }

    try {
      val result = what
      verifyExpectations()
      result
    } catch {
      case ex: Throwable =>
        // do not verify expectations - just clear them. Throw original exception
        // see issue #72
        clearExpectations()
        throw ex
    }
  }

  protected def inAnyOrder[T](what: => T): T = {
    inContext(new UnorderedHandlers)(what)
  }

  protected def inSequence[T](what: => T): T = {
    inContext(new OrderedHandlers)(what)
  }

  //! TODO - https://issues.scala-lang.org/browse/SI-5831
  implicit val _factory = this

  private[scalamock] def add[E <: CallHandler[_]](e: E) = {
    assert(currentExpectationContext != null, "Null expectation context - missing withExpectations?")
    currentExpectationContext.add(e)
    e
  }

  private[scalamock] def reportUnexpectedCall(call: Call) =
    throw newExpectationException(s"Unexpected call: $call\n\n%s".format(errorContext(callLog, expectationContext)), Some(call.target.name))

  private def reportUnsatisfiedExpectation(callLog: CallLog, expectationContext: Handlers) =
    throw newExpectationException(s"Unsatisfied expectation:\n\n%s".format(errorContext(callLog, expectationContext)))

  protected def newExpectationException(message: String, methodName: Option[Symbol] = None): ExpectationException

  private def initializeExpectations() {
    val initialHandlers = new UnorderedHandlers
    callLog = new CallLog

    expectationContext = initialHandlers
    currentExpectationContext = initialHandlers
  }

  private def clearExpectations(): Unit = {
    // to forbid setting expectations after verification is done 
    callLog = null
    expectationContext = null
    currentExpectationContext = null
  }

  private def verifyExpectations() {
    callLog foreach expectationContext.verify _

    val oldCallLog = callLog
    val oldExpectationContext = expectationContext

    clearExpectations()

    if (!oldExpectationContext.isSatisfied)
      reportUnsatisfiedExpectation(oldCallLog, oldExpectationContext)
  }

  private def errorContext(callLog: CallLog, expectationContext: Handlers) =
    s"Expected:\n${expectationContext}\n\nActual:\n${callLog}"

  private def inContext[T](context: Handlers)(what: => T): T = {
    currentExpectationContext.add(context)
    val prevContext = currentExpectationContext
    currentExpectationContext = context
    val r = what
    currentExpectationContext = prevContext
    r
  }

}
