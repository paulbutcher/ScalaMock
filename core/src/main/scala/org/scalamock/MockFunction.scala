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
trait NiceToString { self: MockFunction =>

  override def toString = name.name.toString
}

abstract class MockFunction(protected val factory: MockFactoryBase, protected val name: Symbol) {
  
  def handle(arguments: Product) = {
    factory.logCall(this, arguments)
    null
  }
}

class MockFunction0[R](factory: MockFactoryBase, name: Symbol)
  extends MockFunction(factory, name) with Function0[R] with NiceToString {

  def apply() = handle(None).asInstanceOf[R]
}

class MockFunction1[T1, R](factory: MockFactoryBase, name: Symbol)
  extends MockFunction(factory, name) with Function1[T1, R] with NiceToString {

  def apply(v1: T1) = handle(Tuple1(v1)).asInstanceOf[R]
}

class MockFunction2[T1, T2, R](factory: MockFactoryBase, name: Symbol)
  extends MockFunction(factory, name) with Function2[T1, T2, R] with NiceToString {

  def apply(v1: T1, v2: T2) = handle((v1, v2)).asInstanceOf[R]
}