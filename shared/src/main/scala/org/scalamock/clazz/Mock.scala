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

import org.scalamock.context.MockContext
import org.scalamock.function._
import org.scalamock.util.Defaultable

trait Mock {
  import scala.language.experimental.macros
  import scala.language.implicitConversions
  
  def mock[T](implicit mockContext: MockContext) : T = macro MockImpl.mock[T]
  def stub[T](implicit mockContext: MockContext): T = macro MockImpl.stub[T]

  def mock[T](mockName: String)(implicit mockContext: MockContext) : T = macro MockImpl.mockWithName[T]
  def stub[T](mockName: String)(implicit mockContext: MockContext): T = macro MockImpl.stubWithName[T]

  implicit def toMockFunction0[R: Defaultable](f: () => R): MockFunction0[R] = macro MockImpl.toMockFunction0[R]
  implicit def toMockFunction1[T1, R: Defaultable](f: T1 => R): MockFunction1[T1, R] = macro MockImpl.toMockFunction1[T1, R]
  implicit def toMockFunction2[T1, T2, R: Defaultable](f: (T1, T2) => R): MockFunction2[T1, T2, R] = macro MockImpl.toMockFunction2[T1, T2, R]
  implicit def toMockFunction3[T1, T2, T3, R: Defaultable](f: (T1, T2, T3) => R): MockFunction3[T1, T2, T3, R] = macro MockImpl.toMockFunction3[T1, T2, T3, R]
  implicit def toMockFunction4[T1, T2, T3, T4, R: Defaultable](f: (T1, T2, T3, T4) => R): MockFunction4[T1, T2, T3, T4, R] = macro MockImpl.toMockFunction4[T1, T2, T3, T4, R]
  implicit def toMockFunction5[T1, T2, T3, T4, T5, R: Defaultable](f: (T1, T2, T3, T4, T5) => R): MockFunction5[T1, T2, T3, T4, T5, R] = macro MockImpl.toMockFunction5[T1, T2, T3, T4, T5, R]
  implicit def toMockFunction6[T1, T2, T3, T4, T5, T6, R: Defaultable](f: (T1, T2, T3, T4, T5, T6) => R): MockFunction6[T1, T2, T3, T4, T5, T6, R] = macro MockImpl.toMockFunction6[T1, T2, T3, T4, T5, T6, R]
  implicit def toMockFunction7[T1, T2, T3, T4, T5, T6, T7, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7) => R): MockFunction7[T1, T2, T3, T4, T5, T6, T7, R] = macro MockImpl.toMockFunction7[T1, T2, T3, T4, T5, T6, T7, R]
  implicit def toMockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8) => R): MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R] = macro MockImpl.toMockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R]
  implicit def toMockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => R): MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R] = macro MockImpl.toMockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]
  implicit def toMockFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => R): MockFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R] = macro MockImpl.toMockFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R]
  implicit def toMockFunction11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => R): MockFunction11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R] = macro MockImpl.toMockFunction11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R]
  implicit def toMockFunction12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => R): MockFunction12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R] = macro MockImpl.toMockFunction12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R]
  implicit def toMockFunction13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => R): MockFunction13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R] = macro MockImpl.toMockFunction13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R]
  implicit def toMockFunction14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => R): MockFunction14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R] = macro MockImpl.toMockFunction14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R]
  implicit def toMockFunction15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => R): MockFunction15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R] = macro MockImpl.toMockFunction15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R]
  implicit def toMockFunction16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => R): MockFunction16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R] = macro MockImpl.toMockFunction16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R]
  implicit def toMockFunction17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => R): MockFunction17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R] = macro MockImpl.toMockFunction17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R]
  implicit def toMockFunction18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => R): MockFunction18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R] = macro MockImpl.toMockFunction18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R]
  implicit def toMockFunction19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => R): MockFunction19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R] = macro MockImpl.toMockFunction19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R]
  implicit def toMockFunction20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => R): MockFunction20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R] = macro MockImpl.toMockFunction20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R]
  implicit def toMockFunction21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => R): MockFunction21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R] = macro MockImpl.toMockFunction21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R]
  implicit def toMockFunction22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => R): MockFunction22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R] = macro MockImpl.toMockFunction22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R]

  implicit def toStubFunction0[R: Defaultable](f: () => R): StubFunction0[R] = macro MockImpl.toStubFunction0[R]
  implicit def toStubFunction1[T1,  R: Defaultable](f: T1 => R): StubFunction1[T1, R] = macro MockImpl.toStubFunction1[T1, R]
  implicit def toStubFunction2[T1, T2, R: Defaultable](f: (T1, T2) => R): StubFunction2[T1, T2, R] = macro MockImpl.toStubFunction2[T1, T2, R]
  implicit def toStubFunction3[T1, T2, T3, R: Defaultable](f: (T1, T2, T3) => R): StubFunction3[T1, T2, T3, R] = macro MockImpl.toStubFunction3[T1, T2, T3, R]
  implicit def toStubFunction4[T1, T2, T3, T4, R: Defaultable](f: (T1, T2, T3, T4) => R): StubFunction4[T1, T2, T3, T4, R] = macro MockImpl.toStubFunction4[T1, T2, T3, T4, R]
  implicit def toStubFunction5[T1, T2, T3, T4, T5, R: Defaultable](f: (T1, T2, T3, T4, T5) => R): StubFunction5[T1, T2, T3, T4, T5, R] = macro MockImpl.toStubFunction5[T1, T2, T3, T4, T5, R]
  implicit def toStubFunction6[T1, T2, T3, T4, T5, T6, R: Defaultable](f: (T1, T2, T3, T4, T5, T6) => R): StubFunction6[T1, T2, T3, T4, T5, T6, R] = macro MockImpl.toStubFunction6[T1, T2, T3, T4, T5, T6, R]
  implicit def toStubFunction7[T1, T2, T3, T4, T5, T6, T7, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7) => R): StubFunction7[T1, T2, T3, T4, T5, T6, T7, R] = macro MockImpl.toStubFunction7[T1, T2, T3, T4, T5, T6, T7, R]
  implicit def toStubFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8) => R): StubFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R] = macro MockImpl.toStubFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R]
  implicit def toStubFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => R): StubFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R] = macro MockImpl.toStubFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]
  implicit def toStubFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => R): StubFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R] = macro MockImpl.toStubFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R]
  implicit def toStubFunction11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => R): StubFunction11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R] = macro MockImpl.toStubFunction11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R]
  implicit def toStubFunction12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => R): StubFunction12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R] = macro MockImpl.toStubFunction12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R]
  implicit def toStubFunction13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => R): StubFunction13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R] = macro MockImpl.toStubFunction13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R]
  implicit def toStubFunction14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => R): StubFunction14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R] = macro MockImpl.toStubFunction14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R]
  implicit def toStubFunction15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => R): StubFunction15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R] = macro MockImpl.toStubFunction15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R]
  implicit def toStubFunction16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => R): StubFunction16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R] = macro MockImpl.toStubFunction16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R]
  implicit def toStubFunction17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => R): StubFunction17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R] = macro MockImpl.toStubFunction17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R]
  implicit def toStubFunction18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => R): StubFunction18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R] = macro MockImpl.toStubFunction18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R]
  implicit def toStubFunction19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => R): StubFunction19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R] = macro MockImpl.toStubFunction19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R]
  implicit def toStubFunction20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => R): StubFunction20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R] = macro MockImpl.toStubFunction20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R]
  implicit def toStubFunction21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => R): StubFunction21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R] = macro MockImpl.toStubFunction21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R]
  implicit def toStubFunction22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => R): StubFunction22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R] = macro MockImpl.toStubFunction22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R]
}


