// Copyright (c) 2011-2025 ScalaMock Contributors (https://github.com/ScalaMock/ScalaMock/graphs/contributors)
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

package org.scalamock.context

import org.scalamock.handlers.{CallHandler, Handlers}

private[scalamock] trait MockContext {
  type ExpectationException <: Throwable

  private[scalamock] var callLog: CallLog = scala.compiletime.uninitialized
  private[scalamock] var currentExpectationContext: Handlers = scala.compiletime.uninitialized
  private[scalamock] var expectationContext: Handlers = scala.compiletime.uninitialized
  private[scalamock] val mockNameGenerator: MockNameGenerator = new MockNameGenerator()

  protected def newExpectationException(message: String, methodName: Option[Symbol] = None): ExpectationException

  private[scalamock] def add[E <: CallHandler[?]](e: E) = {
    assert(currentExpectationContext != null, "Null expectation context - missing withExpectations?")
    currentExpectationContext.add(e)
    e
  }

  private[scalamock] def reportUnexpectedCall(call: Call) =
    throw newExpectationException(s"Unexpected call: $call\n\n${errorContext(callLog, expectationContext)}", Some(call.target.name))

  private[scalamock] def reportUnsatisfiedExpectation(callLog: CallLog, expectationContext: Handlers) =
    throw newExpectationException(s"Unsatisfied expectation:\n\n${errorContext(callLog, expectationContext)}")

  private[scalamock] def errorContext(callLog: CallLog, expectationContext: Handlers) =
    s"Expected:\n$expectationContext\n\nActual:\n$callLog"

  /** Generates unique names for mocks, stubs, and mock functions */
  def generateMockDefaultName(prefix: String): Symbol = mockNameGenerator.generateMockName(prefix)
}
