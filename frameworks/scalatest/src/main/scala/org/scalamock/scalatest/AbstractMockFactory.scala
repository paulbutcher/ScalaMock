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

package org.scalamock.scalatest

import org.scalamock.MockFactoryBase
import org.scalatest.{Outcome, Reporter, Stopper, Suite, SuiteMixin, Tracker}
import org.scalatest.exceptions.TestFailedException

trait AbstractMockFactory extends SuiteMixin with MockFactoryBase { this: Suite =>
  
  type ExpectationException = TestFailedException
  
  override def withFixture(test: NoArgTest): Outcome = {

    if (autoVerify)
      withExpectations { test() }
    else
      test()
  }
  
  protected def newExpectationException(message: String, methodName: Option[Symbol]) = 
    new TestFailedException(_ => Some(message), None, {e =>
        e.getStackTrace indexWhere { s =>
          !s.getClassName.startsWith("org.scalamock") && !s.getClassName.startsWith("org.scalatest") &&
          !(s.getMethodName == "newExpectationException") && !(s.getMethodName == "reportUnexpectedCall") &&
          !(methodName.isDefined && s.getMethodName == methodName.get.name)
        }
      })

  protected var autoVerify = true
}
