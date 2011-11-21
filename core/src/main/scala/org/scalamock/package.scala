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

package org

/**
 * =ScalaMock: Native Scala mocking=
 *
 * ScalaMock supports three different mocking styles:
 *
 *   - Function mocks.
 *
 *   - Proxy (type-unsafe) mocks.
 *
 *   - Generated (type-safe) mocks.
 *
 * In all cases, mix the relevant `MockFactory` trait into your test class. For ScalaTest use
 * [[org.scalamock.scalatest.MockFactory]], and for JUnit3 use [[org.scalamock.junit3.MockFactory]].
 *
 * ==Function mocks==
 *
 * Function mocks are created with `mockFunction`. The following, for example, creates a mock
 * function taking a single `Int` argument and returning a `String`:
 *
 * {{{
 * val m = mockFunction[Int, String]
 * }}}
 *
 * Expectations then can be set on a mock function. The following, for example, says that we expect our
 * mock to be called once with the argument `42`, and that when called like that it should return the
 * value `"Forty two"`:
 * 
 * {{{
 * m expects (42) returning "Forty two" once
 * }}}
 *
 * ==Proxy mocks==
 *
 * Proxy mocks can only be used to mock traits and interfaces. To mock classes, singleton/companion
 * objects etc, please use generated mocks.
 * To use proxy mocks, mix [[org.scalamock.ProxyMockFactory]] into your test suite.
 * Proxy mocks are created with `mock`. The following, for example, creates a mock which implements
 * all the `Turtle` trait (interface):
 *
 * {{{
 * val m = mock[Turtle]
 * }}}
 *
 * Expectations can then be set on each of the methods within those traits. For example:
 * 
 * {{{
 * m expects 'setPosition withArgs (10.0, 10.0)
 * m expects 'forward withArgs (5.0)
 * m expects 'getPosition returning (15.0, 10.0)
 * }}}
 *
 * By default, an expectation accepts any arguments and a single call. The following two statements are equivalent:
 *
 * {{{
 * m expects 'forward withArgs (*) once
 * m expects 'forward
 * }}}
 *
 * As a convenience, proxy mocks also support the `stubs` method. The following two statements are equivalent:
 *
 * {{{
 * m expects 'forward anyNumberOfTimes
 * m stubs 'forward
 * }}}
 *
 * ==Generated mocks==
 *
 * Generated mocks rely on the ScalaMock compiler plugin. See
 * [[http://www.paulbutcher.com/2011/10/scalamock-step-by-step/ full worked example]].
 *
 * Classes that are going to be mocked need to be declared with the [[org.scalamock.annotation.mock]]
 * annotation. To mock a class together with its companion object, use
 * [[org.scalamock.annotation.mockWithCompanion]]. To mock a singleton object, use
 * [[org.scalamock.annotation.mockObject]].
 *
 * As well as `MockFactory`, your test class also needs to mix in `GeneratedMockFactory`.
 *
 * Create a mock object with `mock`:
 *
 * {{{
 * val m = mock[Turtle]
 *
 * m.expects.forward(10.0) twice
 * }}}
 *
 * Create a mock object (singleton or companion) with mockObject:
 *
 * {{{
 * val m = mockObject(Turtle)
 *
 * m.expects.createTurtle
 * }}}
 *
 * To mock construtor invocation, use `newInstance`:
 *
 * {{{
 * val m = mock[Turtle]
 * 
 * m.expects.newInstance('blue)
 * m.expects.forward(10.0)
 * }}}
 * 
 * ==Expectations==
 *
 * Expectations can be set on the arguments a function or method is called with and how many times
 * it should be called. In addition, mocks can be instructed to return a particular value or throw
 * an exception when that expectation is met.
 *
 * ===Arguments===
 *
 * To specify expected arguments for a functional mock, use `expects`. To specify expected
 * arguments for a proxy mock, use `withArgs` or `withArguments`.
 *
 * If no expected arguments are given, mocks accept any arguments.
 *
 * To specify arguments that should simply be tested for equality, provide the expected arguments
 * as a tuple:
 *
 * {{{
 * m expects ("this", "that")
 * }}}
 *
 * ScalaMock currently supports two types of generalized matching: ''wildcards'' and ''epsilon 
 * matching''.
 *
 * ====Wildcards====
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
 * ====Epsilon matching====
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
 * ====Predicate matching====
 *
 * More complicated argument matching can be implemented by using `where` to pass a predicate:
 *
 * {{{
 * m = mockFunction[Double, Double, Unit]
 * m expects { where _ < _ }
 * }}}
 *
 * {{{
 * m = mock[Turtle]
 * m expects 'setPosition where { (x: Double, y: Double) => x < y }
 * }}}
 *
 * ===Return value===
 *
 * Mocks can be instructed to return a specific value with `returns` or `returning`:
 *
 * {{{
 * m1 returns 42
 * m2 expects ("this", "that") returning "the other"
 * }}}
 *
 * If no return value is specified, functional mocks return `null.asInstanceOf[R]` where `R` is the
 * return type (which equates to `0` for `Int`, `0.0` for `Double` etc.).
 *
 * If no return value is specified, proxy mocks return `null`. This works correctly for most return
 * types, but not for methods returning primitive types (`Int`, `Double` etc.), where returning 
 * `null` leads to a `NullPointerException`. So you will need to explicitly specify a return value
 * for such methods. This restriction may be lifted in the future.
 *
 * You can return a computed value (or throw a computed exception) with `onCall`, for example:
 *
 * {{{
 * val mockIncrement = mockFunction[Int, Int]
 * m expects (*) onCall { x: Int => x + 1 }
 * }}}
 *
 * ===Exceptions===
 *
 * Instead of a return value, a mock can be instructed to throw:
 * {{{
 * m expects ("this", "that") throws new RuntimeException("what's that?")
 * }}}
 *
 * ===Call count===
 *
 * By default, mocks expect one or more calls (i.e. only fail if the function or method is never
 * called). An exact number of calls or a range can be set with `repeat`:
 *
 * {{{
 * m1 returns 42 repeat 3 to 7
 * m2 expects (3) repeat 10
 * }}}
 *
 * There are various aliases for common expectations and styles:
 *
 * {{{
 * m1 expects ("this", "that") once
 * m2 returns "foo" noMoreThanTwice
 * m3 expects (42) repeated 3 times
 * }}}
 *
 * For a full list, see [[org.scalamock.Expectation]].
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
 * val m1 = mock[Turtle]
 * val m2 = mock[Turtle]
 *
 * inSequence {
 *   m1.expects.setPosition(0.0, 0.0)
 *   m1.expects.penDown
 *   m1.expects.forward(10.0)
 *   m1.expects.penUp
 * }
 * inSequence {
 *   m2.expects.setPosition(1.0, 1.0)
 *   m2.expects.turn(90.0)
 *   m2.expects.forward(1.0)
 *   m2.expects.getPosition returning (2.0, 1.0)
 * }
 *
 * m2.setPosition(1.0, 1.0)
 * m1.setPosition(0.0, 0.0)
 * m1.penDown
 * m2.turn(90.0)
 * m1.forward(10.0)
 * m2.forward(1.0)
 * m1.penUp
 * expect((2.0, 1.0)) { m2.getPosition }
 * }}}
 *
 * To specify that there is no constraint on ordering, use `inAnyOrder` (there is an implicit
 * `inAnyOrder` at the top level). Calls to `inSequence` and `inAnyOrder` can be arbitrarily
 * nested. For example:
 *
 * {{{
 * m.expects.a
 * inSequence {
 *   m.expects.b
 *   inAnyOrder {
 *     m.expects.c
 *     inSequence {
 *       m.expects.d
 *       m.expects.e
 *     }
 *     m.expects.f
 *   }
 *   m.expects.g
 * }}}
 *
 * == Debugging ==
 *
 * If faced with a difficult to debug failing expectation, consider mixing 
 * one or both of the [[org.scalamock.VerboseErrors]] or [[org.scalamock.CallLogging]] traits
 * into your test suite:
 * 
 * {{{
 * class MyTest extends Suite with MockFactory with VerboseErrors with CallLogging
 * }}}
 */
package object scalamock
