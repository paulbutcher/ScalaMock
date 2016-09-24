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

package org.scalamock.clazz

import org.scalamock.clazz.MockFunctionFinder.findMockFunction
import org.scalamock.context.MockContext
import org.scalamock.function._
import org.scalamock.util.{Defaultable, MacroAdapter}

object MockImpl {
  import MacroAdapter.Context

  def mock[T: c.WeakTypeTag](c: Context)(mockContext: c.Expr[MockContext]): c.Expr[T] = {
    val maker = MockMaker[T](c)(mockContext, stub = false, mockName = None)
    maker.make
  }

  def stub[T: c.WeakTypeTag](c: Context)(mockContext: c.Expr[MockContext]): c.Expr[T] = {
    val maker = MockMaker[T](c)(mockContext, stub = true, mockName = None)
    maker.make
  }

  def mockWithName[T: c.WeakTypeTag](c: Context)(mockName: c.Expr[String])(mockContext: c.Expr[MockContext]): c.Expr[T] = {
    val maker = MockMaker[T](c)(mockContext, stub = false, mockName = Some(mockName))
    maker.make
  }

  def stubWithName[T: c.WeakTypeTag](c: Context)(mockName: c.Expr[String])(mockContext: c.Expr[MockContext]): c.Expr[T] = {
    val maker = MockMaker[T](c)(mockContext, stub = true, mockName = Some(mockName))
    maker.make
  }

  def MockMaker[T: c.WeakTypeTag](c: Context)(mockContext: c.Expr[MockContext], stub: Boolean, mockName: Option[c.Expr[String]]) = {
    val m = new MockMaker[c.type](c)
    new m.MockMakerInner[T](mockContext, stub, mockName)
  }

  def toMockFunction0[R: c.WeakTypeTag](c: Context)(f: c.Expr[() => R])(evidence$1: c.Expr[Defaultable[R]]) =
    findMockFunction[() => R, MockFunction0[R]](c)(f, List())

  def toMockFunction1[T1: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[T1 => R])(evidence$2: c.Expr[Defaultable[R]]) =
    findMockFunction[T1 => R, MockFunction1[T1, R]](c)(f, List(c.weakTypeOf[T1]))

  def toMockFunction2[T1: c.WeakTypeTag, T2: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2) => R])(evidence$3: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2) => R, MockFunction2[T1, T2, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2]))

  def toMockFunction3[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3) => R])(evidence$4: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3) => R, MockFunction3[T1, T2, T3, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3]))

  def toMockFunction4[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4) => R])(evidence$5: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4) => R, MockFunction4[T1, T2, T3, T4, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4]))

  def toMockFunction5[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5) => R])(evidence$6: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5) => R, MockFunction5[T1, T2, T3, T4, T5, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5]))

  def toMockFunction6[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6) => R])(evidence$7: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6) => R, MockFunction6[T1, T2, T3, T4, T5, T6, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6]))

  def toMockFunction7[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7) => R])(evidence$8: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7) => R, MockFunction7[T1, T2, T3, T4, T5, T6, T7, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7]))

  def toMockFunction8[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8) => R])(evidence$9: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8) => R, MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8]))

  def toMockFunction9[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9) => R])(evidence$10: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9) => R, MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9]))

  def toMockFunction10[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => R])(evidence$10: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => R, MockFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10]))

  def toMockFunction11[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => R])(evidence$11: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => R, MockFunction11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11]))

  def toMockFunction12[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => R])(evidence$12: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => R, MockFunction12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12]))

  def toMockFunction13[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => R])(evidence$13: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => R, MockFunction13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13]))

  def toMockFunction14[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => R])(evidence$14: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => R, MockFunction14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14]))

  def toMockFunction15[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => R])(evidence$15: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => R, MockFunction15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15]))

  def toMockFunction16[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => R])(evidence$16: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => R, MockFunction16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16]))

  def toMockFunction17[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => R])(evidence$17: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => R, MockFunction17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17]))

  def toMockFunction18[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => R])(evidence$18: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => R, MockFunction18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18]))

  def toMockFunction19[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => R])(evidence$19: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => R, MockFunction19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19]))

  def toMockFunction20[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag, T20: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => R])(evidence$20: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => R, MockFunction20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19], c.weakTypeOf[T20]))

  def toMockFunction21[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag, T20: c.WeakTypeTag, T21: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => R])(evidence$21: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => R, MockFunction21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19], c.weakTypeOf[T20], c.weakTypeOf[T21]))

  def toMockFunction22[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag, T20: c.WeakTypeTag, T21: c.WeakTypeTag, T22: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => R])(evidence$22: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => R, MockFunction22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19], c.weakTypeOf[T20], c.weakTypeOf[T21], c.weakTypeOf[T22]))

  def toStubFunction0[R: c.WeakTypeTag](c: Context)(f: c.Expr[() => R])(evidence$20: c.Expr[Defaultable[R]]) =
    findMockFunction[() => R, StubFunction0[R]](c)(f, List())

  def toStubFunction1[T1: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[T1 => R])(evidence$21: c.Expr[Defaultable[R]]) =
    findMockFunction[T1 => R, StubFunction1[T1, R]](c)(f, List(c.weakTypeOf[T1]))

  def toStubFunction2[T1: c.WeakTypeTag, T2: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2) => R])(evidence$22: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2) => R, StubFunction2[T1, T2, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2]))

  def toStubFunction3[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3) => R])(evidence$23: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3) => R, StubFunction3[T1, T2, T3, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3]))

  def toStubFunction4[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4) => R])(evidence$24: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4) => R, StubFunction4[T1, T2, T3, T4, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4]))

  def toStubFunction5[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5) => R])(evidence$25: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5) => R, StubFunction5[T1, T2, T3, T4, T5, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5]))

  def toStubFunction6[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6) => R])(evidence$26: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6) => R, StubFunction6[T1, T2, T3, T4, T5, T6, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6]))

  def toStubFunction7[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7) => R])(evidence$27: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7) => R, StubFunction7[T1, T2, T3, T4, T5, T6, T7, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7]))

  def toStubFunction8[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8) => R])(evidence$28: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8) => R, StubFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8]))

  def toStubFunction9[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9) => R])(evidence$29: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9) => R, StubFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9]))

  def toStubFunction10[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => R])(evidence$210: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => R, StubFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10]))

  def toStubFunction11[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => R])(evidence$211: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => R, StubFunction11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11]))

  def toStubFunction12[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => R])(evidence$212: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => R, StubFunction12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12]))

  def toStubFunction13[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => R])(evidence$213: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => R, StubFunction13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13]))

  def toStubFunction14[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => R])(evidence$214: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => R, StubFunction14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14]))

  def toStubFunction15[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => R])(evidence$215: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => R, StubFunction15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15]))

  def toStubFunction16[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => R])(evidence$216: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => R, StubFunction16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16]))

  def toStubFunction17[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => R])(evidence$217: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => R, StubFunction17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17]))

  def toStubFunction18[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => R])(evidence$218: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => R, StubFunction18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18]))

  def toStubFunction19[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => R])(evidence$219: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => R, StubFunction19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19]))

  def toStubFunction20[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag, T20: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => R])(evidence$220: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => R, StubFunction20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19], c.weakTypeOf[T20]))

  def toStubFunction21[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag, T20: c.WeakTypeTag, T21: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => R])(evidence$221: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => R, StubFunction21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19], c.weakTypeOf[T20], c.weakTypeOf[T21]))

  def toStubFunction22[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag, T20: c.WeakTypeTag, T21: c.WeakTypeTag, T22: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => R])(evidence$222: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => R, StubFunction22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19], c.weakTypeOf[T20], c.weakTypeOf[T21], c.weakTypeOf[T22]))
}
