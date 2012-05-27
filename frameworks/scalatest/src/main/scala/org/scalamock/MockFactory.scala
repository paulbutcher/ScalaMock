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

package org.scalamock

import org.scalatest.AbstractSuite
import org.scalatest.Reporter
import org.scalatest.Stopper
import org.scalatest.Suite
import org.scalatest.Tracker

/** Trait that can be mixed into a [[http://www.scalatest.org/ ScalaTest]] suite to provide
  * mocking support.
  *
  * See [[org.scalamock]] for overview documentation.
  */
trait MockFactory extends AbstractSuite with MockFactoryBase { this: Suite =>
  
  // Copied from BeforeAndAfterEach:
  // On advice from Bill Venners, we shouldn't use BeforeAndAfterEach here to 
  // ensure that it's properly stackable
  abstract protected override def runTest(testName: String, reporter: Reporter, stopper: Stopper, 
    configMap: Map[String, Any], tracker: Tracker) {

    var thrownException: Option[Throwable] = None

    resetExpectations
    try {
      super.runTest(testName, reporter, stopper, configMap, tracker)
    }
    catch {
      case e: Exception => thrownException = Some(e)
    }
    finally {
      try {
        if (autoVerify)
          verifyExpectations

        thrownException match {
          case Some(e) => throw e
          case None =>
        }
      }
      catch {
        case laterException: Exception =>
          thrownException match { // If both run and afterAll throw an exception, report the test exception
            case Some(earlierException) => throw earlierException
            case None => throw laterException
          }
      }
    }
  }

  protected var autoVerify = true
}
