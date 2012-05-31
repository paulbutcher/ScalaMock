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