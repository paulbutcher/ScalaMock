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

package com.paulbutcher.test.mock

import com.paulbutcher.test.{ IsolatedSpec, TestTrait }
import org.scalamock.function.MockFunction

class MockNamingTest extends IsolatedSpec {

  def getMockMethodName(method: MockFunction) = method.toString
  val m = mock[TestTrait]

  behavior of "Mock"

  it should "have a sensible method name when mocking a method without parameters" in {
    // TODO IMO this is not sensible name - it should be either TestTrait.noParams or m.noParams (see issue #82)
    getMockMethodName(m.noParams _) shouldBe "noParams"
  }

  it should "have a sensible method name when mocking one parameter method" in {
    getMockMethodName(m.oneParam _) shouldBe "oneParam"
  }

  it should "have a sensible method name when mocking an operator" in {
    getMockMethodName(m.+ _) shouldBe "$plus" // TODO could be better
  }

  it should "have a sensible method name when mocking curried method" in {
    getMockMethodName(m.curried(_: Int)(_: Double)) shouldBe "curried"
  }

  it should "have a sensible method name when mocking polymorphic method" in {
    getMockMethodName(m.polymorphic _) shouldBe "polymorphic"
  }

  it should "have a sensible method name when mocking overloaded method" in {
    getMockMethodName(m.overloaded(_: Int)) shouldBe "overloaded"
  }
}
