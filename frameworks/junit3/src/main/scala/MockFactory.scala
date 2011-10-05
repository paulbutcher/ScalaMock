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

package com.borachio.junit3

import com.borachio.AbstractMockFactory
import junit.framework.TestCase

/** Trait that can be mixed into a [[http://www.junit.org/ JUnit3]] suite to provide
  * mocking support.
  *
  * See [[com.borachio]] for overview documentation.
  *
  * Borachio requires that you call reset expectations before each test and 
  * verify them afterwards. You can achieve this in two different ways: with
  * the `withExpectations` method (recommended), or by overriding `setUp` and `tearDown`.
  *
  * == withExpectations ==
  *
  * {{{
  * def testSomething {
  *   withExpectations {
  *     // Setup expectations
  *     // Exercise code under test
  *   }
  * }
  * }}}
  *
  * == setUp and tearDown ==
  *
  * This is not the recommended approach, because JUnit calls `tearDown` even if a
  * test fails, and exceptions in `tearDown` override exceptions thrown by the test.
  * This will result in the original cause of the failure being masked.
  *
  * {{{
  * override def setUp() {
  *   resetExpectations
  * }
  *
  * override def tearDown() {
  *   verifyExpectations
  * }
  *
  * def testSomething {
  *   // Setup expectations
  *   // Exercise code under test
  * }
  * }}}
  */
trait MockFactory extends AbstractMockFactory { this: TestCase =>

  protected def withExpectations(what: => Unit) {
    resetExpectations
    what
    verifyExpectations
  }
}
