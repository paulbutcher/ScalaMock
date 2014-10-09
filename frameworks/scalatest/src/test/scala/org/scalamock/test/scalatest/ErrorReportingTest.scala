// Copyright (c) 2011-2014 Paul Butcher
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

import org.scalamock.scalatest.MockFactory
import org.scalamock.test.mockable.TestTrait
import org.scalatest.Args
import org.scalatest.FlatSpec
import org.scalatest.FunSuite
import org.scalatest.Reporter
import org.scalatest.ShouldMatchers
import org.scalatest.Suite
import org.scalatest.events.Event
import org.scalatest.events.TestFailed
import org.scalatest.exceptions.TestFailedException

/**
 *  Tests that errors are reported correctly in ScalaTest suites
 */
class ErrorReporting extends FlatSpec with ShouldMatchers {

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

  "ScalaTest suite" should "report unexpected call correctly" in {
    class TestedSuite extends FunSuite with MockFactory {
      test("execute block of code") {
        val mockedTrait = mock[TestTrait]
        mockedTrait.oneParamMethod(3)
      }
    }

    val outcome = runTestCase[TestedSuite](new TestedSuite)
    val errorMessage = getErrorMessage[TestFailedException](outcome)
    errorMessage should startWith("Unexpected call")
  }

  // TODO it was broken before issue #25 was fixed. I will fix it later
  it should "report unexpected call correctly when expectations are set" in {
    class TestedSuite extends FunSuite with MockFactory {
      test("execute block of code") {
        val mockedTrait = mock[TestTrait]
        (mockedTrait.oneParamMethod _).expects(1).returning("one")
        mockedTrait.oneParamMethod(3)
      }
    }

    // Unexpected call should be reported by ScalaTest
    val outcome = runTestCase[TestedSuite](new TestedSuite)
    val errorMessage = getErrorMessage[TestFailedException](outcome)
    errorMessage should startWith("Unexpected call")
  }

  it should "not hide NullPointerException" in {
    class TestedSuite extends FunSuite with MockFactory {
      test("execute block of code") {
        val mockedTrait = mock[TestTrait]
        (mockedTrait.oneParamMethod _).expects(1).returning("one")
        throw new NullPointerException;
      }
    }

    val outcome = runTestCase[TestedSuite](new TestedSuite)
    // NullPointerException should be reported by ScalaTest
    getThrowable[NullPointerException](outcome) shouldBe a[NullPointerException]
  }
}
