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

import org.scalamock.MockFactoryBase
import org.specs2.execute.{Failure, FailureException, Result}
import org.specs2.main.ArgumentsShortcuts
import org.specs2.specification.{AroundExample, Fragments, SpecificationStructure}

/**
 * A trait that can be mixed into a [[http://etorreborre.github.com/specs2/ Specs2]] specification to provide
 * mocking support.
 */
trait MockFactory extends MockFactoryBase with AroundExample { self: ArgumentsShortcuts =>
  
  type ExpectationException = FailureException

  protected def around[T <% Result](body: => T) = {
    if (autoVerify)
      withExpectations { body }
    else
      body
  }

  protected def newExpectationException(message: String, methodName: Option[Symbol]) =
    new ExpectationException(new Failure(message))

  protected var autoVerify = true
}