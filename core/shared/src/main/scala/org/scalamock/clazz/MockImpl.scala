// Copyright (c) ScalaMock Contributors (https://github.com/paulbutcher/ScalaMock/graphs/contributors)
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
import org.scalamock.function.*
import org.scalamock.util.Defaultable

import scala.quoted.*


@scala.annotation.experimental
private[clazz] object MockImpl:
  def mock[T: Type](mockContext: Expr[MockContext])(using quotes: Quotes): Expr[T] =
    MockMaker.instance[T](MockType.Mock, mockContext, name = None)

  def stub[T: Type](mockContext: Expr[MockContext])(using quotes: Quotes): Expr[T] =
    MockMaker.instance[T](MockType.Stub, mockContext, name = None)

  def mockWithName[T: Type](mockName: Expr[String])(mockContext: Expr[MockContext])(using quotes: Quotes): Expr[T] =
    MockMaker.instance[T](MockType.Mock, mockContext, Some(mockName))

  def stubWithName[T: Type](mockName: Expr[String])(mockContext: Expr[MockContext])(using quotes: Quotes): Expr[T] =
    MockMaker.instance[T](MockType.Stub, mockContext, Some(mockName))


  def toMockFunction0[R: Type](f: Expr[() => R])(evidence$1: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[MockFunction0[R]](f)

  def toMockFunction1[T1: Type, R: Type](f: Expr[T1 => R])(evidence$2: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[MockFunction1[T1, R]](f)

  def toMockFunction2[T1: Type, T2: Type, R: Type](f: Expr[(T1, T2) => R])(evidence$3: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[MockFunction2[T1, T2, R]](f)

  def toMockFunction3[T1: Type, T2: Type, T3: Type, R: Type](f: Expr[(T1, T2, T3) => R])(evidence$4: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[MockFunction3[T1, T2, T3, R]](f)

  def toMockFunction4[T1: Type, T2: Type, T3: Type, T4: Type, R: Type](f: Expr[(T1, T2, T3, T4) => R])(evidence$5: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[MockFunction4[T1, T2, T3, T4, R]](f)

  def toMockFunction5[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5) => R])(evidence$6: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[MockFunction5[T1, T2, T3, T4, T5, R]](f)

  def toMockFunction6[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6) => R])(evidence$7: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[MockFunction6[T1, T2, T3, T4, T5, T6, R]](f)

  def toMockFunction7[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7) => R])(evidence$8: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[MockFunction7[T1, T2, T3, T4, T5, T6, T7, R]](f)

  def toMockFunction8[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8) => R])(evidence$9: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R]](f)

  def toMockFunction9[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9) => R])(evidence$10: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]](f)

  def toMockFunction10[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => R])(evidence$10: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[MockFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R]](f)

  def toMockFunction11[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => R])(evidence$11: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[MockFunction11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R]](f)

  def toMockFunction12[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, T12: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => R])(evidence$12: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[MockFunction12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R]](f)

  def toMockFunction13[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, T12: Type, T13: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => R])(evidence$13: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[MockFunction13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R]](f)

  def toMockFunction14[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, T12: Type, T13: Type, T14: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => R])(evidence$14: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[MockFunction14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R]](f)

  def toMockFunction15[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, T12: Type, T13: Type, T14: Type, T15: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => R])(evidence$15: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[MockFunction15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R]](f)

  def toMockFunction16[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, T12: Type, T13: Type, T14: Type, T15: Type, T16: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => R])(evidence$16: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[MockFunction16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R]](f)

  def toMockFunction17[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, T12: Type, T13: Type, T14: Type, T15: Type, T16: Type, T17: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => R])(evidence$17: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[MockFunction17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R]](f)

  def toMockFunction18[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, T12: Type, T13: Type, T14: Type, T15: Type, T16: Type, T17: Type, T18: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => R])(evidence$18: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[MockFunction18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R]](f)

  def toMockFunction19[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, T12: Type, T13: Type, T14: Type, T15: Type, T16: Type, T17: Type, T18: Type, T19: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => R])(evidence$19: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[MockFunction19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R]](f)

  def toMockFunction20[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, T12: Type, T13: Type, T14: Type, T15: Type, T16: Type, T17: Type, T18: Type, T19: Type, T20: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => R])(evidence$20: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[MockFunction20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R]](f)

  def toMockFunction21[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, T12: Type, T13: Type, T14: Type, T15: Type, T16: Type, T17: Type, T18: Type, T19: Type, T20: Type, T21: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => R])(evidence$21: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[MockFunction21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R]](f)

  def toMockFunction22[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, T12: Type, T13: Type, T14: Type, T15: Type, T16: Type, T17: Type, T18: Type, T19: Type, T20: Type, T21: Type, T22: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => R])(evidence$22: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[MockFunction22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R]](f)

  def toStubFunction0[R: Type](f: Expr[() => R])(evidence$20: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[StubFunction0[R]](f)

  def toStubFunction1[T1: Type, R: Type](f: Expr[T1 => R])(evidence$21: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[StubFunction1[T1, R]](f)

  def toStubFunction2[T1: Type, T2: Type, R: Type](f: Expr[(T1, T2) => R])(evidence$22: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[StubFunction2[T1, T2, R]](f)

  def toStubFunction3[T1: Type, T2: Type, T3: Type, R: Type](f: Expr[(T1, T2, T3) => R])(evidence$23: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[StubFunction3[T1, T2, T3, R]](f)

  def toStubFunction4[T1: Type, T2: Type, T3: Type, T4: Type, R: Type](f: Expr[(T1, T2, T3, T4) => R])(evidence$24: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[StubFunction4[T1, T2, T3, T4, R]](f)

  def toStubFunction5[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5) => R])(evidence$25: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[StubFunction5[T1, T2, T3, T4, T5, R]](f)

  def toStubFunction6[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6) => R])(evidence$26: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[StubFunction6[T1, T2, T3, T4, T5, T6, R]](f)

  def toStubFunction7[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7) => R])(evidence$27: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[StubFunction7[T1, T2, T3, T4, T5, T6, T7, R]](f)

  def toStubFunction8[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8) => R])(evidence$28: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[StubFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R]](f)

  def toStubFunction9[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9) => R])(evidence$29: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[StubFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]](f)

  def toStubFunction10[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => R])(evidence$210: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[StubFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R]](f)

  def toStubFunction11[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => R])(evidence$211: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[StubFunction11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R]](f)

  def toStubFunction12[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, T12: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => R])(evidence$212: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[StubFunction12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R]](f)

  def toStubFunction13[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, T12: Type, T13: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => R])(evidence$213: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[StubFunction13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R]](f)

  def toStubFunction14[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, T12: Type, T13: Type, T14: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => R])(evidence$214: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[StubFunction14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R]](f)

  def toStubFunction15[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, T12: Type, T13: Type, T14: Type, T15: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => R])(evidence$215: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[StubFunction15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R]](f)

  def toStubFunction16[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, T12: Type, T13: Type, T14: Type, T15: Type, T16: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => R])(evidence$216: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[StubFunction16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R]](f)

  def toStubFunction17[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, T12: Type, T13: Type, T14: Type, T15: Type, T16: Type, T17: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => R])(evidence$217: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[StubFunction17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R]](f)

  def toStubFunction18[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, T12: Type, T13: Type, T14: Type, T15: Type, T16: Type, T17: Type, T18: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => R])(evidence$218: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[StubFunction18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R]](f)

  def toStubFunction19[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, T12: Type, T13: Type, T14: Type, T15: Type, T16: Type, T17: Type, T18: Type, T19: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => R])(evidence$219: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[StubFunction19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R]](f)

  def toStubFunction20[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, T12: Type, T13: Type, T14: Type, T15: Type, T16: Type, T17: Type, T18: Type, T19: Type, T20: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => R])(evidence$220: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[StubFunction20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R]](f)

  def toStubFunction21[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, T12: Type, T13: Type, T14: Type, T15: Type, T16: Type, T17: Type, T18: Type, T19: Type, T20: Type, T21: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => R])(evidence$221: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[StubFunction21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R]](f)

  def toStubFunction22[T1: Type, T2: Type, T3: Type, T4: Type, T5: Type, T6: Type, T7: Type, T8: Type, T9: Type, T10: Type, T11: Type, T12: Type, T13: Type, T14: Type, T15: Type, T16: Type, T17: Type, T18: Type, T19: Type, T20: Type, T21: Type, T22: Type, R: Type](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => R])(evidence$222: Expr[Defaultable[R]])(using quotes: Quotes) =
    findMockFunction[StubFunction22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R]](f)

