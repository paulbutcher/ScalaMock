package org.scalamock.specs2

import org.scalamock.MockFactoryBase
import org.specs2.specification.{BeforeExample, AfterExample}

/**
 * A trait that can be mixed into a [[http://etorreborre.github.com/specs2/ Specs2]] specification to provide
 * mocking support.
 */
trait MockFactory extends MockFactoryBase with BeforeExample with AfterExample {
  protected def before = resetExpectations
  protected def after = if (autoverify) verifyExpectations
  protected var autoverify = true
}