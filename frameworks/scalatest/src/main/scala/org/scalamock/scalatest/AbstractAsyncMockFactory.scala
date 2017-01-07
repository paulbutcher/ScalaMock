package org.scalamock.scalatest

import org.scalatest._
import org.scalatest.exceptions.{StackDepthException, TestFailedException}

import scala.concurrent.Future

/**
  * Created by Luiz Guilherme D'Abruzzo Pereira <luiz290788@gmail.com> on 22/12/16.
  */
trait AbstractAsyncMockFactory extends AsyncTestSuiteMixin with AsyncMockFactoryBase with AsyncTestSuite {

  type ExpectationException = TestFailedException

  abstract override def withFixture(test: NoArgAsyncTest): FutureOutcome = {
    if (autoVerify) {
      new FutureOutcome(withExpectations(super.withFixture(test).toFuture).recoverWith({
        case t: Throwable => Future.successful(Exceptional(t))
      }))
    } else {
      super.withFixture(test)
    }
  }

  protected def newExpectationException(message: String, methodName: Option[Symbol]) =
    new TestFailedException((_: StackDepthException) => Some(message), None, failedCodeStackDepthFn(methodName))

  protected var autoVerify = true

}
