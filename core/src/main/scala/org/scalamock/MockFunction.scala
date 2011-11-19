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

package org.scalamock

class MockFunction(protected val factory: MockFactoryBase, name: Symbol) {

  def handle(arguments: Array[Any]) = factory.handle(this, arguments)
  
  private[scalamock] def canHandle(that: MockFunction) = this == that
  
  private[scalamock] val failIfUnexpected = true
  
  private[scalamock] def toTypeSafeExpectation0[R] = factory.add(new TypeSafeExpectation0[R](this))
  private[scalamock] def toTypeSafeExpectation1[T1, R] = factory.add(new TypeSafeExpectation1[T1, R](this))
  private[scalamock] def toTypeSafeExpectation2[T1, T2, R] = factory.add(new TypeSafeExpectation2[T1, T2, R](this))
  private[scalamock] def toTypeSafeExpectation3[T1, T2, T3, R] = factory.add(new TypeSafeExpectation3[T1, T2, T3, R](this))
  private[scalamock] def toTypeSafeExpectation4[T1, T2, T3, T4, R] = factory.add(new TypeSafeExpectation4[T1, T2, T3, T4, R](this))
  private[scalamock] def toTypeSafeExpectation5[T1, T2, T3, T4, T5, R] = factory.add(new TypeSafeExpectation5[T1, T2, T3, T4, T5, R](this))
  private[scalamock] def toTypeSafeExpectation6[T1, T2, T3, T4, T5, T6, R] = factory.add(new TypeSafeExpectation6[T1, T2, T3, T4, T5, T6, R](this))
  private[scalamock] def toTypeSafeExpectation7[T1, T2, T3, T4, T5, T6, T7, R] = factory.add(new TypeSafeExpectation7[T1, T2, T3, T4, T5, T6, T7, R](this))
  private[scalamock] def toTypeSafeExpectation8[T1, T2, T3, T4, T5, T6, T7, T8, R] = factory.add(new TypeSafeExpectation8[T1, T2, T3, T4, T5, T6, T7, T8, R](this))
  private[scalamock] def toTypeSafeExpectation9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R] = factory.add(new TypeSafeExpectation9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](this))
  private[scalamock] def toTypeSafeExpectation10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R] = factory.add(new TypeSafeExpectation10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](this))
}

class MockFunction0[R](factory: MockFactoryBase, name: Symbol)
  extends MockFunction(factory, name) with Function0[R] {

  override def toString = name.name.toString

  def apply() = handle(Array()).asInstanceOf[R]
}

class MockFunction1[T1, R](factory: MockFactoryBase, name: Symbol) 
  extends MockFunction(factory, name) with Function1[T1, R] {

  override def toString = name.name.toString

  def apply(v1: T1) = handle(Array(v1)).asInstanceOf[R]
}

class MockFunction2[T1, T2, R](factory: MockFactoryBase, name: Symbol) 
  extends MockFunction(factory, name) with Function2[T1, T2, R] {
  
  override def toString = name.name.toString

  def apply(v1: T1, v2: T2) = handle(Array(v1, v2)).asInstanceOf[R]
}

class MockFunction3[T1, T2, T3, R](factory: MockFactoryBase, name: Symbol) 
  extends MockFunction(factory, name) with Function3[T1, T2, T3, R] {
  
  override def toString = name.name.toString

  def apply(v1: T1, v2: T2, v3: T3) = handle(Array(v1, v2, v3)).asInstanceOf[R]
}

class MockFunction4[T1, T2, T3, T4, R](factory: MockFactoryBase, name: Symbol) 
  extends MockFunction(factory, name) with Function4[T1, T2, T3, T4, R] {
  
  override def toString = name.name.toString

  def apply(v1: T1, v2: T2, v3: T3, v4: T4) = handle(Array(v1, v2, v3, v4)).asInstanceOf[R]
}

class MockFunction5[T1, T2, T3, T4, T5, R](factory: MockFactoryBase, name: Symbol) 
  extends MockFunction(factory, name) with Function5[T1, T2, T3, T4, T5, R] {
  
  override def toString = name.name.toString

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5) = handle(Array(v1, v2, v3, v4, v5)).asInstanceOf[R]
}

class MockFunction6[T1, T2, T3, T4, T5, T6, R](factory: MockFactoryBase, name: Symbol) 
  extends MockFunction(factory, name) with Function6[T1, T2, T3, T4, T5, T6, R] {
  
  override def toString = name.name.toString

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6) = handle(Array(v1, v2, v3, v4, v5, v6)).asInstanceOf[R]
}

class MockFunction7[T1, T2, T3, T4, T5, T6, T7, R](factory: MockFactoryBase, name: Symbol) 
  extends MockFunction(factory, name) with Function7[T1, T2, T3, T4, T5, T6, T7, R] {
  
  override def toString = name.name.toString

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7) = handle(Array(v1, v2, v3, v4, v5, v6, v7)).asInstanceOf[R]
}

class MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](factory: MockFactoryBase, name: Symbol) 
  extends MockFunction(factory, name) with Function8[T1, T2, T3, T4, T5, T6, T7, T8, R] {
  
  override def toString = name.name.toString

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8) = handle(Array(v1, v2, v3, v4, v5, v6, v7, v8)).asInstanceOf[R]
}

class MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](factory: MockFactoryBase, name: Symbol) 
  extends MockFunction(factory, name) with Function9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R] {
  
  override def toString = name.name.toString

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8, v9: T9) = handle(Array(v1, v2, v3, v4, v5, v6, v7, v8, v9)).asInstanceOf[R]
}

class MockFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](factory: MockFactoryBase, name: Symbol) 
  extends MockFunction(factory, name) with Function10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R] {
  
  override def toString = name.name.toString

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8, v9: T9, v10: T10) = handle(Array(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10)).asInstanceOf[R]
}
