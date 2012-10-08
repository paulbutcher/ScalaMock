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

package com.paulbutcher.test

import reflect.runtime.universe.TypeTag

trait TestTrait {
  def nullary: String
  def noParams(): String
  def oneParam(x: Int): String
  def twoParams(x: Int, y: Double): String
  
  def overloaded(x: Int): String
  def overloaded(x: String): String
  def overloaded(x: Int, y: Double): String
  
  def +(x: TestTrait): TestTrait
  
  def curried(x: Int)(y: Double): String
  def polymorphic[T](x: List[T]): String
  def polycurried[T1, T2](x: T1)(y: T2): (T1, T2)
  def polymorphicParam(x: (Int, Double)): String
  def repeatedParam(x: Int, ys: String*): String
  def byNameParam(x: => Int): String
  // def implicitParam(x: Int)(implicit y: Double): String
  
  def upperBound[T <: Product](x: T): Int
  def lowerBound[T >: U, U](x: T, y: List[U]): String
  // def contextBound[T: TypeTag](x: T): String
  // def viewBound[T <% Ordered[T]](x: T, y: T): Boolean
  
  def withImplementation(x: Int) = x * x
  
  var aVar: String
  var concreteVar = "foo"

  val aVal: String
  val concreteVal = "foo"
  
  trait Embedded {
    def m(x: Int, y: Double): String

    trait ATrait
    def innerTrait(): ATrait
    def outerTrait(): TestTrait.this.ATrait
    def innerTraitProjected(): TestTrait#Embedded#ATrait
    def outerTraitProjected(): TestTrait#ATrait
  }
  
  trait ATrait
  
  def referencesEmbedded(): Embedded
}