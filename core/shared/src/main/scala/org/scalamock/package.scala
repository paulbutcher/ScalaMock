// Copyright (c) 2011-2015 ScalaMock Contributors (https://github.com/paulbutcher/ScalaMock/graphs/contributors)
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

package org

/**
 * ==ScalaMock: Native Scala Mocking==
 * 
 * To use ScalaMock, you need the relevant `MockFactoryBase` trait implementation:
 * 
 *  - for ScalaTest, use [[org.scalamock.scalatest.MockFactory]],
 *  - for Specs2 use [[org.scalamock.specs2.IsolatedMockFactory]] or 
 *    [[org.scalamock.specs2.MockContext]] 
 * 
 * At present, ScalaMock can only mock traits, Java interfaces, and non-final 
 * classes that define a default constructor. A future version will be able to 
 * mock any class, and singleton/companion objects. 
 *
 * ScalaMock supports two different mocking styles - ''expectations first'' and
 * ''record then verify''. These styles can be mixed within a single test.
 * 
 * ==Expectations-First Style==
 * 
 * In the expectations-first style, expectations are set on mock objects before
 * exercising the system under test. If these expectations are not met, the 
 * test fails.
 * 
 * A mock function that supports this style is created with `mockFunction`. For 
 * example, to create a mock function taking a single `Int` argument and 
 * returning a `String`:
 * 
 * {{{
 * val m = mockFunction[Int, String]
 * }}}
 * 
 * A mock object that supports this style is created with `mock`. For example,
 * to create a mock that implements the `Turtle` trait:
 * 
 * {{{
 * val m = mock[Turtle]
 * }}}
 * 
 * Expectations can then be set using `expects`:
 * 
 * {{{
 * (m.setPosition _).expects(10.0, 10.0)
 * (m.forward _).expects(5.0)
 * (m.getPosition _).expects().returning(15.0, 10.0)
 * 
 * drawLine(m, (10.0, 10.0), (15.0, 10.0))
 * }}}
 * 
 * ==Record-then-Verify (Mockito) Style==
 * 
 * In the record then verify style, expectations are verified after the system
 * under test has executed. 
 * 
 * A stub function that supports this style is created with `stubFunction`. For 
 * example:
 * 
 * {{{
 * val m = stubFunction[Int, String]
 * }}}
 * 
 * A stub object that supports this style is created with `stub`. For example:
 * 
 * {{{
 * val m = stub[Turtle]
 * }}}
 * 
 * Return values that are used by the system under test can be set up by using 
 * `when`. Calls are verified using `verify`:
 * 
 * {{{
 * (m.getPosition _).when().returns(15.0, 10.0)
 * 
 * drawLine(m, (10.0, 10.0), (15.0, 10.0))
 * 
 * (m.setPosition _).verify(10.0, 10.0)
 * (m.forward _).verify(5.0)
 * }}}
 * 
 * ==Argument matching==
 * 
 * ScalaMock supports two types of generalised matching: ''wildcards'' and
 * ''epsilon matching''.
 * 
 * ===Wildcards===
 *
 * Wildcard values are specified with an `*` (asterisk). For example:
 * 
 * {{{
 * m expects ("this", *)
 * }}}
 * 
 * will match any of the following:
 *
 * {{{
 * m("this", 42)
 * m("this", 1.0)
 * m("this", null)
 * }}}
 *
 * ===Epsilon matching===
 *
 * Epsilon matching is useful when dealing with floating point values. An epsilon match is
 * specified with the `~` (tilde) operator:
 *
 * {{{
 * m expects (~42.0)
 * }}}
 *
 * will match:
 *
 * {{{
 * m(42.0)
 * m(42.0001)
 * m(41.9999)
 * }}}
 *
 * but will not match:
 *
 * {{{
 * m(43.0)
 * m(42.1)
 * }}}
 * 
 * ===Repeated parameters===
 * 
 * Repeated parameters are represented as a `Seq`. For example, given:
 * 
 * {{{
 * def takesRepeatedParameter(x: Int, ys: String*)
 * }}}
 *
 * you can set an expectation with:
 * 
 * {{{
 * (m.takesRepeatedParameter _).expects(42, Seq("red", "green", "blue"))
 * }}}
 * 
 * ===Predicate matching===
 *
 * More complicated argument matching can be implemented by using `where` to 
 * pass a predicate:
 *
 * {{{
 * m = mockFunction[Double, Double, Unit]
 * m expects (where { _ < _ })
 * }}}
 * 
 * ===Return values===
 * 
 * By default mocks and stubs return `null`. You can return a computed return 
 * value (or throw a computed exception) with `onCall`:
 * 
 * {{{
 * val mockIncrement = mockFunction[Int, Int]
 * mockIncrement expects (*) onCall { _ + 1 }
 * }}}
 * 
 * ===Overloaded, curried and polymorphic methods===
 * 
 * Overloaded, curried and polymorphic methods can be mocked by specifying 
 * either argument types or type parameters. For example:
 * 
 * {{{
 * trait Foo {
 *   def overloaded(x: Int): String
 *   def overloaded(x: String): String
 *   def overloaded[T](x: T): String
 *   def curried(x: Int)(y: Double): String
 *   def polymorphic[T](x: List[T]): String
 * }
 * }}}
 * 
 * {{{
 * val m = mock[Foo]
 * (m.overloaded(_: Int)).expects(10)
 * (m.overloaded(_: String)).expects("foo")
 * (m.overloaded[Double] _).expects(1.23)
 * (m.curried(_: Int)(_: Double)).expects(10, 1.23)
 * (m.polymorphic(_: List[Int])).expects(List(1, 2, 3))
 * (m.polymorphic[String] _).expects("foo")
 * }}}
 *
 * ===Exceptions===
 *
 * Instead of a return value, mocks and stubs can be instructed to throw:
 * 
 * {{{
 * m expects ("this", "that") throws new RuntimeException("what's that?")
 * }}}
 * 
 * ===Call count===
 *
 * By default, mocks and stubs expect exactly one call. Alternative constraints
 * can be set with `repeat`:
 *
 * {{{
 * m1.expects(42).returns(42).repeat(3 to 7)
 * m2 expects (3) repeat 10
 * }}}
 *
 * There are various aliases for common expectations and styles:
 *
 * {{{
 * m1.expects("this", "that").once
 * m2.expects().returns("foo").noMoreThanTwice
 * m3.expects(42).repeated(3).times
 * }}}
 *
 * For a full list, see [[org.scalamock.handlers.CallHandler]].
 *
 * ==Ordering==
 *
 * By default, expectations can be satisfied in any order. For example:
 *
 * {{{
 * m expects (1)
 * m expects (2)
 * m(2)
 * m(1)
 * }}}
 *
 * A specific sequence can be enforced with `inSequence`:
 * 
 * {{{
 * inSequence {
 *   m expects (1)
 *   m expects (2)
 * }
 * m(2) // throws ExpectationException
 * m(1)
 * }}}
 *
 * Multiple sequences can be specified. As long as the calls within each sequence happen in the
 * correct order, calls within different sequences can be interleaved. For example:
 *
 * {{{
 * inSequence {
 *   m expects (1)
 *   m expects (2)
 * }
 * inSequence {
 *   m expects (3)
 *   m expects (4)
 * }
 *
 * m(3)
 * m(1)
 * m(2)
 * m(4)
 * }}}
 *
 * To specify that there is no constraint on ordering, use `inAnyOrder` (there is an implicit
 * `inAnyOrder` at the top level). Calls to `inSequence` and `inAnyOrder` can be arbitrarily
 * nested. For example:
 *
 * {{{
 * (m.a _).expects()
 * inSequence {
 *   (m.b _).expects()
 *   inAnyOrder {
 *     (m.c _).expects()
 *     inSequence {
 *       (m.d _).expects()
 *       (m.e _).expects()
 *     }
 *     (m.f _).expects()
 *   }
 *   (m.g _).expects()
 * }
 * }}}
 * 
 * == Threads ==
 * 
 * ScalaMock will work with tests that are run in parallel (Specs2 runs tests in parallel by
 * default, and ScalaTest does so with `ParallelTestExecution`).
 * 
 * You can call mocks from other threads within tests, but any such calls must be complete
 * before the test completes - it's an error to call a mock afterwards.
 */
package object scalamock
