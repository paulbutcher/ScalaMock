// Copyright (c) 2011-12 Paul Butcher
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

package org.scalamock.specs2

import org.specs2.execute.{ AsResult, Failure, FailureException, Result }
import org.specs2.main.ArgumentsShortcuts
import org.specs2.specification.{ AroundExample, Fragments, SpecificationStructure }
import org.specs2.specification.Fragment
import org.specs2.mutable.FragmentsBuilder
import org.specs2.mutable.Around
import org.scalamock.MockFactoryBase

/** Base trait for MockContext and IsolatedMockFactory */
trait MockContextBase extends MockFactoryBase {

  type ExpectationException = FailureException

  protected override def newExpectationException(message: String, methodName: Option[Symbol]) =
    new ExpectationException(new Failure(message))

  protected def wrapAsResult[T: AsResult](body: => T) = {
    AsResult(withExpectations { body })
  }
}

/**
 * Fixture-context that should be created per test-case basis
 */
trait MockContext extends MockContextBase with Around {

  override def around[T: AsResult](body: => T) = {
    wrapAsResult[T] { body }
  }
}

/**
 * A trait that can be mixed into a [[http://etorreborre.github.com/specs2/ Specs2]] specification to provide
 * mocking support.
 */
trait IsolatedMockFactory extends AroundExample with MockContextBase { self: ArgumentsShortcuts =>
  isolated

  override def around[T: AsResult](body: => T) = {
    wrapAsResult[T] { body }
  }
}

/**
 * A trait that can be mixed into a [[http://etorreborre.github.com/specs2/ Specs2]] specification to provide
 * mocking support.
 *
 * MockFactory does not work well in with thread pools and futures. Fixture-contexts support is also broken.
 */
@deprecated("MockFactory is buggy. Please use IsolatedMockFactory or MockContext instead", "3.2")
trait MockFactory extends AroundExample with MockContextBase { self: ArgumentsShortcuts =>

  override def around[T: AsResult](body: => T) = {
    wrapAsResult[T] { body }
  }
}

