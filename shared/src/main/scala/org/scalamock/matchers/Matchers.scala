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

package org.scalamock.matchers

import org.scalamock.context.MockContext
import org.scalamock.function._

import scala.reflect.ClassTag

trait Matchers { this: MockContext =>
  import scala.language.implicitConversions

  protected def where[T1](matcher: T1 => Boolean) = new FunctionAdapter1(matcher)
  protected def where[T1, T2](matcher: (T1, T2) => Boolean) = new FunctionAdapter2(matcher)
  protected def where[T1, T2, T3](matcher: (T1, T2, T3) => Boolean) = new FunctionAdapter3(matcher)
  protected def where[T1, T2, T3, T4](matcher: (T1, T2, T3, T4) => Boolean) = new FunctionAdapter4(matcher)
  protected def where[T1, T2, T3, T4, T5](matcher: (T1, T2, T3, T4, T5) => Boolean) = new FunctionAdapter5(matcher)
  protected def where[T1, T2, T3, T4, T5, T6](matcher: (T1, T2, T3, T4, T5, T6) => Boolean) = new FunctionAdapter6(matcher)
  protected def where[T1, T2, T3, T4, T5, T6, T7](matcher: (T1, T2, T3, T4, T5, T6, T7) => Boolean) = new FunctionAdapter7(matcher)
  protected def where[T1, T2, T3, T4, T5, T6, T7, T8](matcher: (T1, T2, T3, T4, T5, T6, T7, T8) => Boolean) = new FunctionAdapter8(matcher)
  protected def where[T1, T2, T3, T4, T5, T6, T7, T8, T9](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => Boolean) = new FunctionAdapter9(matcher)
  protected def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => Boolean) = new FunctionAdapter10(matcher)
  protected def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => Boolean) = new FunctionAdapter11(matcher)
  protected def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => Boolean) = new FunctionAdapter12(matcher)
  protected def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => Boolean) = new FunctionAdapter13(matcher)
  protected def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => Boolean) = new FunctionAdapter14(matcher)
  protected def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => Boolean) = new FunctionAdapter15(matcher)
  protected def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => Boolean) = new FunctionAdapter16(matcher)
  protected def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => Boolean) = new FunctionAdapter17(matcher)
  protected def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => Boolean) = new FunctionAdapter18(matcher)
  protected def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => Boolean) = new FunctionAdapter19(matcher)
  protected def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => Boolean) = new FunctionAdapter20(matcher)
  protected def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => Boolean) = new FunctionAdapter21(matcher)
  protected def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => Boolean) = new FunctionAdapter22(matcher)

  protected def argThat[T](clue: String)(predicate: T => Boolean)
    (implicit classTag: ClassTag[T]): MatcherBase = new ArgThat[T](predicate, clue = Some(clue))

  protected def argThat[T](predicate: T => Boolean)
    (implicit classTag: ClassTag[T]): MatcherBase = new ArgThat[T](predicate, clue = None)

  protected def argAssert[T](clue: String)(assertions: T => Unit)
    (implicit classTag: ClassTag[T]): MatcherBase = new ArgAssert[T](assertions, clue = Some(clue))

  protected def argAssert[T](assertions: T => Unit)
    (implicit classTag: ClassTag[T]): MatcherBase = new ArgAssert[T](assertions, clue = None)


  protected def * = new MatchAny

  protected class EpsilonMatcher(d: Double) {
    def unary_~() = new MatchEpsilon(d)
  }

  protected implicit def doubleToEpsilon(d: Double) = new EpsilonMatcher(d)
  protected implicit def toMockParameter[T](v: T) = new MockParameter(v)
  protected implicit def matcherBaseToMockParameter[T](m: MatcherBase) = new MockParameter[T](m)
}
