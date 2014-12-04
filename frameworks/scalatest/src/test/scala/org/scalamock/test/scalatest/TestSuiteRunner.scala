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
