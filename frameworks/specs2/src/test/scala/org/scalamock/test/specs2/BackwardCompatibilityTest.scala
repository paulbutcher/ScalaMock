// Copyright (c) 2011-2012 Paul Butcher
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

package org.scalamock.test.specs2

import org.scalamock.test.mockable.TestTrait
import org.specs2.mutable.Specification
import org.scalamock.specs2.MockFactory

/**
 * Tests that old specs2 tests suites work with ScalaMock 3.2 using deprecated MockFactory
 */ 
class BackwardCompatibilityTest extends Specification with MockFactory {

  "Deprecated MockFactory" can {
    "be used with old test suites" in {
      val mockedTrait = mock[TestTrait]
      (mockedTrait.oneParamMethod _).expects(1).returning("one")
      (mockedTrait.oneParamMethod _).expects(2).returning("two")

      mockedTrait.oneParamMethod(1) must_== "one"
      mockedTrait.oneParamMethod(2) must_== "two"
    }
  }
}
