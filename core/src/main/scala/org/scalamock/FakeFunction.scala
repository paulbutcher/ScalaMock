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

package org.scalamock

// This has to be a separate trait, not a method in MockFunction, because
// otherwise linearization will choose the MockFunctionN toString
trait NiceToString { self: FakeFunction =>

  override def toString = name.name.toString
}

abstract class FakeFunction(protected val factory: MockFactoryBase, protected val name: Symbol) {
  
  def handle(arguments: Product): Any = {
    val call = new Call(this, arguments)
    factory.handle(call) match {
      case Some(retVal) => retVal
      case None => onUnexpected(call)
    }
  }
  
  protected def onUnexpected(call: Call): Any
}

abstract class FakeFunction0[R](factory: MockFactoryBase, name: Symbol)
  extends FakeFunction(factory, name) with Function0[R] with NiceToString {

  def apply() = handle(None).asInstanceOf[R]
}

abstract class FakeFunction1[T1, R](factory: MockFactoryBase, name: Symbol)
  extends FakeFunction(factory, name) with Function1[T1, R] with NiceToString {

  def apply(v1: T1) = handle(Tuple1(v1)).asInstanceOf[R]
}

abstract class FakeFunction2[T1, T2, R](factory: MockFactoryBase, name: Symbol)
  extends FakeFunction(factory, name) with Function2[T1, T2, R] with NiceToString {

  def apply(v1: T1, v2: T2) = handle((v1, v2)).asInstanceOf[R]
}

abstract class FakeFunction3[T1, T2, T3, R](factory: MockFactoryBase, name: Symbol)
  extends FakeFunction(factory, name) with Function3[T1, T2, T3, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3) = handle((v1, v2, v3)).asInstanceOf[R]
}

abstract class FakeFunction4[T1, T2, T3, T4, R](factory: MockFactoryBase, name: Symbol)
  extends FakeFunction(factory, name) with Function4[T1, T2, T3, T4, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4) = handle((v1, v2, v3, v4)).asInstanceOf[R]
}

abstract class FakeFunction5[T1, T2, T3, T4, T5, R](factory: MockFactoryBase, name: Symbol)
  extends FakeFunction(factory, name) with Function5[T1, T2, T3, T4, T5, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5) = handle((v1, v2, v3, v4, v5)).asInstanceOf[R]
}

abstract class FakeFunction6[T1, T2, T3, T4, T5, T6, R](factory: MockFactoryBase, name: Symbol)
  extends FakeFunction(factory, name) with Function6[T1, T2, T3, T4, T5, T6, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6) = handle((v1, v2, v3, v4, v5, v6)).asInstanceOf[R]
}

abstract class FakeFunction7[T1, T2, T3, T4, T5, T6, T7, R](factory: MockFactoryBase, name: Symbol)
  extends FakeFunction(factory, name) with Function7[T1, T2, T3, T4, T5, T6, T7, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7) = handle((v1, v2, v3, v4, v5, v6, v7)).asInstanceOf[R]
}

abstract class FakeFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](factory: MockFactoryBase, name: Symbol)
  extends FakeFunction(factory, name) with Function8[T1, T2, T3, T4, T5, T6, T7, T8, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8) = handle((v1, v2, v3, v4, v5, v6, v7, v8)).asInstanceOf[R]
}

abstract class FakeFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](factory: MockFactoryBase, name: Symbol)
  extends FakeFunction(factory, name) with Function9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8, v9: T9) = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9)).asInstanceOf[R]
}
