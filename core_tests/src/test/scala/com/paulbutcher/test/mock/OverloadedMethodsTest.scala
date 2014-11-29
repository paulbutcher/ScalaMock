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

import com.paulbutcher.test._

class OverloadedMethodsTest extends IsolatedSpec {

  behavior of "Mock"

  it should "mock traits with overloaded methods which have different number of type params" in { // test for issue #85
    trait Foo {
      def overloaded[T](x: T): String
      def overloaded(x: String): String
    }

    val fooMock = mock[Foo]
    (fooMock.overloaded[Double] _) expects (1.0) returning "one"
    fooMock.overloaded(1.0) shouldBe "one"

    (fooMock.overloaded(_: String)) expects ("2") returning "two"
    fooMock.overloaded("2") shouldBe "two"
  }

  it should "mock traits with overloaded methods which have different number of type params (2)" in {
    trait Foo {
      def overloaded[T](x: T): String
      def overloaded[T](x: T, y: String): String
    }

    val fooMock = mock[Foo]

    (fooMock.overloaded[Double]: Double => String) expects (1.0) returning "one"
    fooMock.overloaded(1.0) shouldBe "one"

    (fooMock.overloaded[Double]: (Double, String) => String).expects(2.0, "foo") returning "two"
    fooMock.overloaded(2.0, "foo") shouldBe "two"
  }

  it should "mock traits with overloaded methods which have different number of type params (3)" in {
    trait Foo {
      def overloaded[T](x: T): String
      def overloaded[T, U](x: T, y: U): String
    }

    val fooMock = mock[Foo]

    (fooMock.overloaded[Double]: Double => String) expects (1.0) returning "one"
    fooMock.overloaded(1.0) shouldBe "one"

    (fooMock.overloaded[Double, String]: (Double, String) => String).expects(2.0, "foo") returning "two"
    fooMock.overloaded(2.0, "foo") shouldBe "two"
  }

  it should "mock traits with overloaded methods which have different number of type params (4)" in {
    trait Foo {
      def overloaded[T](x: T, y: String): String
      def overloaded[T, U](x: T, y: U): String
    }

    val fooMock = mock[Foo]

    (fooMock.overloaded[Double]: (Double, String) => String) expects (1.0, "foo") returning "one"
    fooMock.overloaded(1.0, "foo") shouldBe "one"

    (fooMock.overloaded[String, Double]: (String, Double) => String).expects("foo", 2.0) returning "two"
    fooMock.overloaded("foo", 2.0) shouldBe "two"
  }
}
