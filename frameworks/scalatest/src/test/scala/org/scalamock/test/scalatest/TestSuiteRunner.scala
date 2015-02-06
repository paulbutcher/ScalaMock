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

package org.scalamock.test.scalatest
import org.scalatest.events.{ Event, TestFailed }
import org.scalatest.ShouldMatchers
import org.scalatest.{ Args, Reporter, Suite }

import scala.language.postfixOps

trait TestSuiteRunner { this: ShouldMatchers =>

  /** Executes single ScalaTest test case and returns its outcome (i.e. either TestSucccess or TestFailure) */
  def runTestCase[T <: Suite](suite: T): Event = {
    class TestReporter extends Reporter {
      var lastEvent: Option[Event] = None
      override def apply(e: Event): Unit = { lastEvent = Some(e) }
    }

    val reporter = new TestReporter
    suite.run(None, Args(reporter))
    reporter.lastEvent.get
  }

  def getThrowable[ExnT <: Throwable](event: Event)(implicit m: Manifest[ExnT]): ExnT = {
    event shouldBe a[TestFailed]

    val testCaseError = event.asInstanceOf[TestFailed].throwable.get
    testCaseError shouldBe a[ExnT]
    testCaseError.asInstanceOf[ExnT]
  }

  def getErrorMessage[ExnT <: Throwable](event: Event)(implicit m: Manifest[ExnT]): String = {
    getThrowable[ExnT](event).getMessage()
  }
}
