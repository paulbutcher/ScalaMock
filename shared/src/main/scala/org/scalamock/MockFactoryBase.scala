// Copyright (c) 2011-2015 ScalaMock Contributors (https://github.com/paulbutcher/ScalaMock/graphs/contributors)
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

import org.scalamock.context.{CallLog, MockContext}
import org.scalamock.handlers.{Handlers, OrderedHandlers, UnorderedHandlers}

import scala.util.control.NonFatal

trait MockFactoryBase extends AbstractMockFactoryBase with MockContext {
  import scala.language.implicitConversions

  initializeExpectations()

  override protected def withExpectations[T](what: => T): T = {
    if (expectationContext == null) {
      // we don't reset expectations for the first test case to allow
      // defining expectations in Suite scope and writing tests in OneInstancePerTest/isolated style 
      initializeExpectations()
    }

    try {
      val result = what
      verifyExpectations()
      result
    } catch {
      case NonFatal(ex) =>
        // do not verify expectations - just clear them. Throw original exception
        // see issue #72
        clearExpectations()
        throw ex
    }
  }

  override protected def inAnyOrder[T](what: => T): T = {
    inContext(new UnorderedHandlers)(what)
  }

  override protected def inSequence[T](what: => T): T = {
    inContext(new OrderedHandlers)(what)
  }

  override protected def inAnyOrderWithLogging[T](what: => T) =
    inContext(new UnorderedHandlers(logging = true))(what)

  override protected def inSequenceWithLogging[T](what: => T) =
    inContext(new OrderedHandlers(logging = true))(what)

  // https://issues.scala-lang.org/browse/SI-5831
  implicit val _factory: MockFactoryBase = this

  private def initializeExpectations(): Unit = {
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

  private def verifyExpectations(): Unit = {
    callLog foreach expectationContext.verify _

    val oldCallLog = callLog
    val oldExpectationContext = expectationContext

    clearExpectations()

    if (!oldExpectationContext.isSatisfied)
      reportUnsatisfiedExpectation(oldCallLog, oldExpectationContext)
  }

  private def inContext[T](context: Handlers)(what: => T): T = {
    currentExpectationContext.add(context)
    val prevContext = currentExpectationContext
    currentExpectationContext = context
    val r = what
    currentExpectationContext = prevContext
    r
  }

}
