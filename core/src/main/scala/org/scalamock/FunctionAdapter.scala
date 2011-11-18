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

private[scalamock] class FunctionAdapter1[T1, R](f: T1 => R) extends (Array[Any] => R) {
  def apply(args: Array[Any]) = {
    require(args.length == 1)
    f(args(0).asInstanceOf[T1])
  }
}

private[scalamock] class FunctionAdapter2[T1, T2, R](f: (T1, T2) => R) extends (Array[Any] => R) {
  def apply(args: Array[Any]) = {
    require(args.length == 2)
    f(args(0).asInstanceOf[T1], args(1).asInstanceOf[T2])
  }
}

private[scalamock] class FunctionAdapter3[T1, T2, T3, R](f: (T1, T2, T3) => R) extends (Array[Any] => R) {
  def apply(args: Array[Any]) = {
    require(args.length == 3)
    f(args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3])
  }
}

private[scalamock] class FunctionAdapter4[T1, T2, T3, T4, R](f: (T1, T2, T3, T4) => R) extends (Array[Any] => R) {
  def apply(args: Array[Any]) = {
    require(args.length == 4)
    f(args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3], args(3).asInstanceOf[T4])
  }
}

private[scalamock] class FunctionAdapter5[T1, T2, T3, T4, T5, R](f: (T1, T2, T3, T4, T5) => R) extends (Array[Any] => R) {
  def apply(args: Array[Any]) = {
    require(args.length == 5)
    f(args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3], args(3).asInstanceOf[T4], args(4).asInstanceOf[T5])
  }
}

private[scalamock] class FunctionAdapter6[T1, T2, T3, T4, T5, T6, R](f: (T1, T2, T3, T4, T5, T6) => R) extends (Array[Any] => R) {
  def apply(args: Array[Any]) = {
    require(args.length == 6)
    f(args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3], args(3).asInstanceOf[T4], args(4).asInstanceOf[T5], args(5).asInstanceOf[T6])
  }
}

private[scalamock] class FunctionAdapter7[T1, T2, T3, T4, T5, T6, T7, R](f: (T1, T2, T3, T4, T5, T6, T7) => R) extends (Array[Any] => R) {
  def apply(args: Array[Any]) = {
    require(args.length == 7)
    f(args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3], args(3).asInstanceOf[T4], args(4).asInstanceOf[T5], args(5).asInstanceOf[T6], args(6).asInstanceOf[T7])
  }
}

private[scalamock] class FunctionAdapter8[T1, T2, T3, T4, T5, T6, T7, T8, R](f: (T1, T2, T3, T4, T5, T6, T7, T8) => R) extends (Array[Any] => R) {
  def apply(args: Array[Any]) = {
    require(args.length == 8)
    f(args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3], args(3).asInstanceOf[T4], args(4).asInstanceOf[T5], args(5).asInstanceOf[T6], args(6).asInstanceOf[T7], args(7).asInstanceOf[T8])
  }
}

private[scalamock] class FunctionAdapter9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => R) extends (Array[Any] => R) {
  def apply(args: Array[Any]) = {
    require(args.length == 9)
    f(args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3], args(3).asInstanceOf[T4], args(4).asInstanceOf[T5], args(5).asInstanceOf[T6], args(6).asInstanceOf[T7], args(7).asInstanceOf[T8], args(8).asInstanceOf[T9])
  }
}

private[scalamock] class FunctionAdapter10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => R) extends (Array[Any] => R) {
  def apply(args: Array[Any]) = {
    require(args.length == 10)
    f(args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3], args(3).asInstanceOf[T4], args(4).asInstanceOf[T5], args(5).asInstanceOf[T6], args(6).asInstanceOf[T7], args(7).asInstanceOf[T8], args(8).asInstanceOf[T9], args(9).asInstanceOf[T10])
  }
}