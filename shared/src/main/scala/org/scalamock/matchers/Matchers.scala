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
import org.scalamock.matchers.ArgCapture.{Capture, CaptureMatcher}

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

  protected def assertArgs[T1](matcher: (T1) => Any): FunctionAdapter1[T1, Boolean] = {
    val f: (T1) => Boolean = (t1) => { matcher(t1); true }
    new FunctionAdapter1[T1, Boolean](f)
  }

  protected def assertArgs[T1, T2](matcher: (T1, T2) => Any): FunctionAdapter2[T1, T2, Boolean] = {
    val f: (T1, T2) => Boolean = (t1, t2) => { matcher(t1, t2); true }
    new FunctionAdapter2[T1, T2, Boolean](f)
  }

  protected def assertArgs[T1, T2, T3](matcher: (T1, T2, T3) => Unit): FunctionAdapter3[T1, T2, T3, Boolean] = {
    val f: (T1, T2, T3) => Boolean = (t1, t2, t3) => { matcher(t1, t2, t3); true }
    new FunctionAdapter3[T1, T2, T3, Boolean](f)
  }

  protected def assertArgs[T1, T2, T3, T4](matcher: (T1, T2, T3, T4) => Unit): FunctionAdapter4[T1, T2, T3, T4, Boolean] = {
    val f: (T1, T2, T3, T4) => Boolean = (t1, t2, t3, t4) => { matcher(t1, t2, t3, t4); true }
    new FunctionAdapter4[T1, T2, T3, T4, Boolean](f)
  }

  protected def assertArgs[T1, T2, T3, T4, T5](matcher: (T1, T2, T3, T4, T5) => Unit): FunctionAdapter5[T1, T2, T3, T4, T5, Boolean] = {
    val f: (T1, T2, T3, T4, T5) => Boolean = (t1, t2, t3, t4, t5) => { matcher(t1, t2, t3, t4, t5); true }
    new FunctionAdapter5[T1, T2, T3, T4, T5, Boolean](f)
  }

  protected def assertArgs[T1, T2, T3, T4, T5, T6](matcher: (T1, T2, T3, T4, T5, T6) => Unit): FunctionAdapter6[T1, T2, T3, T4, T5, T6, Boolean] = {
    val f: (T1, T2, T3, T4, T5, T6) => Boolean = (t1, t2, t3, t4, t5, t6) => { matcher(t1, t2, t3, t4, t5, t6); true }
    new FunctionAdapter6[T1, T2, T3, T4, T5, T6, Boolean](f)
  }

  protected def assertArgs[T1, T2, T3, T4, T5, T6, T7](matcher: (T1, T2, T3, T4, T5, T6, T7) => Unit): FunctionAdapter7[T1, T2, T3, T4, T5, T6, T7, Boolean] = {
    val f: (T1, T2, T3, T4, T5, T6, T7) => Boolean = (t1, t2, t3, t4, t5, t6, t7) => { matcher(t1, t2, t3, t4, t5, t6, t7); true }
    new FunctionAdapter7[T1, T2, T3, T4, T5, T6, T7, Boolean](f)
  }

    protected def assertArgs[T1, T2, T3, T4, T5, T6, T7, T8](matcher: (T1, T2, T3, T4, T5, T6, T7, T8) => Unit): FunctionAdapter8[T1, T2, T3, T4, T5, T6, T7, T8, Boolean] = {
    val f: (T1, T2, T3, T4, T5, T6, T7, T8) => Boolean = (t1, t2, t3, t4, t5, t6, t7, t8) => { matcher(t1, t2, t3, t4, t5, t6, t7, t8); true }
    new FunctionAdapter8[T1, T2, T3, T4, T5, T6, T7, T8, Boolean](f)
  }

  protected def assertArgs[T1, T2, T3, T4, T5, T6, T7, T8, T9](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => Unit): FunctionAdapter9[T1, T2, T3, T4, T5, T6, T7, T8, T9, Boolean] = {
    val f: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => Boolean = (t1, t2, t3, t4, t5, t6, t7, t8, t9) => { matcher(t1, t2, t3, t4, t5, t6, t7, t8, t9); true }
    new FunctionAdapter9[T1, T2, T3, T4, T5, T6, T7, T8, T9, Boolean](f)
  }

  protected def assertArgs[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => Unit): FunctionAdapter10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Boolean] = {
    val f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => Boolean = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) => { matcher(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10); true }
    new FunctionAdapter10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Boolean](f)
  }

  protected def assertArgs[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => Unit): FunctionAdapter11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Boolean] = {
    val f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => Boolean = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) => { matcher(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11); true }
    new FunctionAdapter11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Boolean](f)
  }

  protected def assertArgs[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => Unit): FunctionAdapter12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, Boolean] = {
    val f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => Boolean = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) => { matcher(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12); true }
    new FunctionAdapter12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, Boolean](f)
  }

  protected def assertArgs[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => Unit): FunctionAdapter13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, Boolean] = {
    val f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => Boolean = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13) => { matcher(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13); true }
    new FunctionAdapter13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, Boolean](f)
  }

  protected def assertArgs[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => Unit): FunctionAdapter14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, Boolean] = {
    val f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => Boolean = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14) => { matcher(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14); true }
    new FunctionAdapter14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, Boolean](f)
  }

  protected def assertArgs[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => Unit): FunctionAdapter15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, Boolean] = {
    val f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => Boolean = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15) => { matcher(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15); true }
    new FunctionAdapter15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, Boolean](f)
  }

  protected def assertArgs[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => Unit): FunctionAdapter16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, Boolean] = {
    val f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => Boolean = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16) => { matcher(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16); true }
    new FunctionAdapter16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, Boolean](f)
  }

  protected def assertArgs[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => Unit): FunctionAdapter17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, Boolean] = {
    val f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => Boolean = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17) => { matcher(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17); true }
    new FunctionAdapter17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, Boolean](f)
  }

  protected def assertArgs[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => Unit): FunctionAdapter18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, Boolean] = {
    val f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => Boolean = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18) => { matcher(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18); true }
    new FunctionAdapter18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, Boolean](f)
  }

  protected def assertArgs[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => Unit): FunctionAdapter19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, Boolean] = {
    val f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => Boolean = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19) => { matcher(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19); true }
    new FunctionAdapter19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, Boolean](f)
  }

  protected def assertArgs[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => Unit): FunctionAdapter20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, Boolean] = {
    val f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => Boolean = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20) => { matcher(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20); true }
    new FunctionAdapter20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, Boolean](f)
  }

  protected def assertArgs[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => Unit): FunctionAdapter21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, Boolean] = {
    val f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => Boolean = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21) => { matcher(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21); true }
    new FunctionAdapter21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, Boolean](f)
  }

  protected def assertArgs[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => Unit): FunctionAdapter22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, Boolean] = {
    val f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => Boolean = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22) => { matcher(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22); true }
    new FunctionAdapter22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, Boolean](f)
  }


  protected def argThat[T](clue: String)(predicate: T => Boolean)
    (implicit classTag: ClassTag[T]): MatcherBase = new ArgThat[T](predicate, clue = Some(clue))

  protected def argThat[T](predicate: T => Boolean)
    (implicit classTag: ClassTag[T]): MatcherBase = new ArgThat[T](predicate, clue = None)

  protected def argAssert[T](clue: String)(assertions: T => Any)
    (implicit classTag: ClassTag[T]): MatcherBase = new ArgAssert[T](assertions, clue = Some(clue))

  protected def argAssert[T](assertions: T => Any)
    (implicit classTag: ClassTag[T]): MatcherBase = new ArgAssert[T](assertions, clue = None)

  protected def capture[T](cap: Capture[T]) = new CaptureMatcher[T](cap)

  protected def * = new MatchAny

  protected class EpsilonMatcher(d: Double) {
    def unary_~ = new MatchEpsilon(d)
  }

  protected implicit def doubleToEpsilon(d: Double): EpsilonMatcher = new EpsilonMatcher(d)
  protected implicit def toMockParameter[T](v: T): MockParameter[T] = new MockParameter(v)
  protected implicit def matcherBaseToMockParameter[T](m: MatcherBase): MockParameter[T] = new MockParameter[T](m)
}
