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

package org.scalamock.clazz

import org.scalamock.MockFactoryBase
import org.scalamock.function._
import org.scalamock.util.Defaultable

trait Mock {
  import scala.language.experimental.macros
  import scala.language.implicitConversions
  
  def mock[T](implicit factory: MockFactoryBase) : T = macro MockImpl.mock[T]
  
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

  def stub[T](implicit factory: MockFactoryBase): T = macro MockImpl.stub[T]

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
}


