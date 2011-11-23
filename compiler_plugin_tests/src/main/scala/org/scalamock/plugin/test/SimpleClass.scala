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

package org.scalamock.plugin.test

class SimpleClass {
  override def toString = "SimpleClass"

  def nullMethod {}
  def methodWithZeroArguments() = "methodWithZeroArguments"
  def methodWithOneArgument(x: Int) = "methodWithOneArgument: "+ x
  def methodWithTwoArguments(x: Int, y: Int) = "methodWithTwoArguments: "+ (x, y)
  def methodWithNonPrimitiveArgument(x: SimpleClass4) = x.toString
  
  def +(s: String) = toString + s
  def callSimpleClass2(x: Int) = (new SimpleClass2).doSomething(x)
  def nestDeeply(x: Int) = (new SimpleClass2).nestDeeply(x)
  
  def methodReturningFunction(x: Int) = (y: Int) => x * y
  def curriedMethod(a: Int, b: Double)(c: String, d: Boolean) = "curriedMethod: "+ (a, b, c, d)
  def withImplicitParameters(x: Int)(implicit i1: ImplicitTest1, i2: ImplicitTest2) = (x, i1, i2).toString
  
  def polymorphicMethod[A, B](x: Int, a: A, b: B) = (x, a, b)
  def withUpperBound[T <: Product](x: T) = x.productArity
  def withViewBound[T <% Ordered[T]](x: T, y: T) = x < y
  def withContextBound[T: ClassManifest] = "erasure is: "+ classManifest[T].erasure
  
  //! TODO
//  def byName(x: => Unit) = x; x
}

class SimpleClass2 {
  def doSomething(x: Int) = "doSomething: "+ x
  def nestDeeply(x: Int) = (new SimpleClass3).nestDeeply(x)
}

class SimpleClass3 {
  def nestDeeply(x: Int) = (new SimpleClass4).doSomething(x)
}

class SimpleClass4 {
  def doSomething(x: Int) = "doSomething: "+ x
}

class ImplicitTest1
class ImplicitTest2