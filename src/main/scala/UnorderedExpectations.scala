// Copyright (c) 2011 Paul Butcher
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

package com.borachio

import scala.collection.mutable.ListBuffer

private[borachio] class UnorderedExpectations extends Expectations {

  private[borachio] def handle(mock: MockFunction, arguments: Array[Any]): Any = {
    for (handler <- handlers) {
      val r = handler.handle(mock, arguments)
      if (r.isDefined)
        return r.get
    }
    handleUnexpectedCall("Unexpected: "+ mock +" with arguments: "+ arguments.mkString("(", ", ", ")"))
  }
  
  private[borachio] def verify() {
    if (!unexpectedCalls.isEmpty)
      throw new ExpectationException(unexpectedCallsMessage + verboseMessage)

    handlers.foreach { handler =>
      if (!handler.satisfied)
        throw new ExpectationException("Unsatisfied expectation: "+ handler + verboseMessage)
    }
  }
  
  private[borachio] def reset(verbose: Boolean) {
    handlers.clear
    unexpectedCalls.clear
    this.verbose = verbose
  }
  
  private[borachio] def handleUnexpectedCall(message: String) = {
    unexpectedCalls += message
    throw new ExpectationException(unexpectedCallsMessage + verboseMessage)
  }
  
  private[borachio] def verboseMessage = if (verbose) "\n\nExpectations:\n" + toString else ""
  
  private[borachio] def unexpectedCallsMessage = unexpectedCalls.mkString("\n")
  
  override def toString = handlers.map(_.toString).mkString("\n")
  
  private var verbose = false
  private val unexpectedCalls = new ListBuffer[String]
}
