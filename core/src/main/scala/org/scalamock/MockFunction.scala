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

trait MockFunction { self: FakeFunction =>
  
  def handle(arguments: Product): Any = self.factory.handle(new Call(this, arguments)) match {
    case Some(retVal) => retVal
    case None => factory.reportUnexpectedCall
  }
}

class MockFunction0[R](factory: MockFactoryBase, name: Symbol)
  extends FakeFunction0[R](factory, name) with MockFunction {
  
  def expects() = factory.add(new CallHandler0[R])
  def expects(matcher: FunctionAdapter0[Boolean]) = factory.add(new CallHandler0[R](matcher))
}

class MockFunction1[T1, R](factory: MockFactoryBase, name: Symbol)
  extends FakeFunction1[T1, R](factory, name) with MockFunction {

  def expects(v1: MockParameter[T1]) = factory.add(new CallHandler1[T1, R](v1))
  def expects(matcher: FunctionAdapter1[T1, Boolean]) = factory.add(new CallHandler1[T1, R](matcher))
}

class MockFunction2[T1, T2, R](factory: MockFactoryBase, name: Symbol)
  extends FakeFunction2[T1, T2, R](factory, name) with MockFunction {

  def expects(v1: MockParameter[T1], v2: MockParameter[T2]) = factory.add(new CallHandler2[T1, T2, R](v1, v2))
  def expects(matcher: FunctionAdapter2[T1, T2, Boolean]) = factory.add(new CallHandler2[T1, T2, R](matcher))
}