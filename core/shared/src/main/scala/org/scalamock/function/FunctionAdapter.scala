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

package org.scalamock.function

class FunctionAdapter0[R](f: () => R) extends Function1[Product, R] {
  
  def apply(args: Product) = {
    assert(args.productArity == 0)
    f()
  }
}

class FunctionAdapter1[T1, R](f: T1 => R) extends Function1[Product, R] {
  
  def apply(args: Product) = {
    assert(args.productArity == 1)
    f(args.productElement(0).asInstanceOf[T1])
  }
}

class FunctionAdapter2[T1, T2, R](f: (T1, T2) => R) extends Function1[Product, R] {
  
  def apply(args: Product) = {
    assert(args.productArity == 2)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2])
  }
}

class FunctionAdapter3[T1, T2, T3, R](f: (T1, T2, T3) => R) extends Function1[Product, R] {
  
  def apply(args: Product) = {
    assert(args.productArity == 3)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2], args.productElement(2).asInstanceOf[T3])
  }
}

class FunctionAdapter4[T1, T2, T3, T4, R](f: (T1, T2, T3, T4) => R) extends Function1[Product, R] {
  
  def apply(args: Product) = {
    assert(args.productArity == 4)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2], args.productElement(2).asInstanceOf[T3], args.productElement(3).asInstanceOf[T4])
  }
}

class FunctionAdapter5[T1, T2, T3, T4, T5, R](f: (T1, T2, T3, T4, T5) => R) extends Function1[Product, R] {
  
  def apply(args: Product) = {
    assert(args.productArity == 5)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2], args.productElement(2).asInstanceOf[T3], args.productElement(3).asInstanceOf[T4], args.productElement(4).asInstanceOf[T5])
  }
}

class FunctionAdapter6[T1, T2, T3, T4, T5, T6, R](f: (T1, T2, T3, T4, T5, T6) => R) extends Function1[Product, R] {
  
  def apply(args: Product) = {
    assert(args.productArity == 6)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2], args.productElement(2).asInstanceOf[T3], args.productElement(3).asInstanceOf[T4], args.productElement(4).asInstanceOf[T5], args.productElement(5).asInstanceOf[T6])
  }
}

class FunctionAdapter7[T1, T2, T3, T4, T5, T6, T7, R](f: (T1, T2, T3, T4, T5, T6, T7) => R) extends Function1[Product, R] {
  
  def apply(args: Product) = {
    assert(args.productArity == 7)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2], args.productElement(2).asInstanceOf[T3], args.productElement(3).asInstanceOf[T4], args.productElement(4).asInstanceOf[T5], args.productElement(5).asInstanceOf[T6], args.productElement(6).asInstanceOf[T7])
  }
}

class FunctionAdapter8[T1, T2, T3, T4, T5, T6, T7, T8, R](f: (T1, T2, T3, T4, T5, T6, T7, T8) => R) extends Function1[Product, R] {
  
  def apply(args: Product) = {
    assert(args.productArity == 8)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2], args.productElement(2).asInstanceOf[T3], args.productElement(3).asInstanceOf[T4], args.productElement(4).asInstanceOf[T5], args.productElement(5).asInstanceOf[T6], args.productElement(6).asInstanceOf[T7], args.productElement(7).asInstanceOf[T8])
  }
}

class FunctionAdapter9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => R) extends Function1[Product, R] {
  
  def apply(args: Product) = {
    assert(args.productArity == 9)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2], args.productElement(2).asInstanceOf[T3], args.productElement(3).asInstanceOf[T4], args.productElement(4).asInstanceOf[T5], args.productElement(5).asInstanceOf[T6], args.productElement(6).asInstanceOf[T7], args.productElement(7).asInstanceOf[T8], args.productElement(8).asInstanceOf[T9])
  }
}

class FunctionAdapter10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => R) extends Function1[Product, R] {

  def apply(args: Product) = {
    assert(args.productArity == 10)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2], args.productElement(2).asInstanceOf[T3], args.productElement(3).asInstanceOf[T4], args.productElement(4).asInstanceOf[T5], args.productElement(5).asInstanceOf[T6], args.productElement(6).asInstanceOf[T7], args.productElement(7).asInstanceOf[T8], args.productElement(8).asInstanceOf[T9], args.productElement(9).asInstanceOf[T10])
  }
}

class FunctionAdapter11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => R) extends Function1[Product, R] {

  def apply(args: Product) = {
    assert(args.productArity == 11)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2], args.productElement(2).asInstanceOf[T3], args.productElement(3).asInstanceOf[T4], args.productElement(4).asInstanceOf[T5], args.productElement(5).asInstanceOf[T6], args.productElement(6).asInstanceOf[T7], args.productElement(7).asInstanceOf[T8], args.productElement(8).asInstanceOf[T9], args.productElement(9).asInstanceOf[T10], args.productElement(10).asInstanceOf[T11])
  }
}

class FunctionAdapter12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => R) extends Function1[Product, R] {

  def apply(args: Product) = {
    assert(args.productArity == 12)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2], args.productElement(2).asInstanceOf[T3], args.productElement(3).asInstanceOf[T4], args.productElement(4).asInstanceOf[T5], args.productElement(5).asInstanceOf[T6], args.productElement(6).asInstanceOf[T7], args.productElement(7).asInstanceOf[T8], args.productElement(8).asInstanceOf[T9], args.productElement(9).asInstanceOf[T10], args.productElement(10).asInstanceOf[T11], args.productElement(11).asInstanceOf[T12])
  }
}

class FunctionAdapter13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => R) extends Function1[Product, R] {

  def apply(args: Product) = {
    assert(args.productArity == 13)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2], args.productElement(2).asInstanceOf[T3], args.productElement(3).asInstanceOf[T4], args.productElement(4).asInstanceOf[T5], args.productElement(5).asInstanceOf[T6], args.productElement(6).asInstanceOf[T7], args.productElement(7).asInstanceOf[T8], args.productElement(8).asInstanceOf[T9], args.productElement(9).asInstanceOf[T10], args.productElement(10).asInstanceOf[T11], args.productElement(11).asInstanceOf[T12], args.productElement(12).asInstanceOf[T13])
  }
}

class FunctionAdapter14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => R) extends Function1[Product, R] {

  def apply(args: Product) = {
    assert(args.productArity == 14)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2], args.productElement(2).asInstanceOf[T3], args.productElement(3).asInstanceOf[T4], args.productElement(4).asInstanceOf[T5], args.productElement(5).asInstanceOf[T6], args.productElement(6).asInstanceOf[T7], args.productElement(7).asInstanceOf[T8], args.productElement(8).asInstanceOf[T9], args.productElement(9).asInstanceOf[T10], args.productElement(10).asInstanceOf[T11], args.productElement(11).asInstanceOf[T12], args.productElement(12).asInstanceOf[T13], args.productElement(13).asInstanceOf[T14])
  }
}

class FunctionAdapter15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => R) extends Function1[Product, R] {

  def apply(args: Product) = {
    assert(args.productArity == 15)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2], args.productElement(2).asInstanceOf[T3], args.productElement(3).asInstanceOf[T4], args.productElement(4).asInstanceOf[T5], args.productElement(5).asInstanceOf[T6], args.productElement(6).asInstanceOf[T7], args.productElement(7).asInstanceOf[T8], args.productElement(8).asInstanceOf[T9], args.productElement(9).asInstanceOf[T10], args.productElement(10).asInstanceOf[T11], args.productElement(11).asInstanceOf[T12], args.productElement(12).asInstanceOf[T13], args.productElement(13).asInstanceOf[T14], args.productElement(14).asInstanceOf[T15])
  }
}

class FunctionAdapter16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => R) extends Function1[Product, R] {

  def apply(args: Product) = {
    assert(args.productArity == 16)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2], args.productElement(2).asInstanceOf[T3], args.productElement(3).asInstanceOf[T4], args.productElement(4).asInstanceOf[T5], args.productElement(5).asInstanceOf[T6], args.productElement(6).asInstanceOf[T7], args.productElement(7).asInstanceOf[T8], args.productElement(8).asInstanceOf[T9], args.productElement(9).asInstanceOf[T10], args.productElement(10).asInstanceOf[T11], args.productElement(11).asInstanceOf[T12], args.productElement(12).asInstanceOf[T13], args.productElement(13).asInstanceOf[T14], args.productElement(14).asInstanceOf[T15], args.productElement(15).asInstanceOf[T16])
  }
}

class FunctionAdapter17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => R) extends Function1[Product, R] {

  def apply(args: Product) = {
    assert(args.productArity == 17)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2], args.productElement(2).asInstanceOf[T3], args.productElement(3).asInstanceOf[T4], args.productElement(4).asInstanceOf[T5], args.productElement(5).asInstanceOf[T6], args.productElement(6).asInstanceOf[T7], args.productElement(7).asInstanceOf[T8], args.productElement(8).asInstanceOf[T9], args.productElement(9).asInstanceOf[T10], args.productElement(10).asInstanceOf[T11], args.productElement(11).asInstanceOf[T12], args.productElement(12).asInstanceOf[T13], args.productElement(13).asInstanceOf[T14], args.productElement(14).asInstanceOf[T15], args.productElement(15).asInstanceOf[T16], args.productElement(16).asInstanceOf[T17])
  }
}

class FunctionAdapter18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => R) extends Function1[Product, R] {

  def apply(args: Product) = {
    assert(args.productArity == 18)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2], args.productElement(2).asInstanceOf[T3], args.productElement(3).asInstanceOf[T4], args.productElement(4).asInstanceOf[T5], args.productElement(5).asInstanceOf[T6], args.productElement(6).asInstanceOf[T7], args.productElement(7).asInstanceOf[T8], args.productElement(8).asInstanceOf[T9], args.productElement(9).asInstanceOf[T10], args.productElement(10).asInstanceOf[T11], args.productElement(11).asInstanceOf[T12], args.productElement(12).asInstanceOf[T13], args.productElement(13).asInstanceOf[T14], args.productElement(14).asInstanceOf[T15], args.productElement(15).asInstanceOf[T16], args.productElement(16).asInstanceOf[T17], args.productElement(17).asInstanceOf[T18])
  }
}

class FunctionAdapter19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => R) extends Function1[Product, R] {

  def apply(args: Product) = {
    assert(args.productArity == 19)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2], args.productElement(2).asInstanceOf[T3], args.productElement(3).asInstanceOf[T4], args.productElement(4).asInstanceOf[T5], args.productElement(5).asInstanceOf[T6], args.productElement(6).asInstanceOf[T7], args.productElement(7).asInstanceOf[T8], args.productElement(8).asInstanceOf[T9], args.productElement(9).asInstanceOf[T10], args.productElement(10).asInstanceOf[T11], args.productElement(11).asInstanceOf[T12], args.productElement(12).asInstanceOf[T13], args.productElement(13).asInstanceOf[T14], args.productElement(14).asInstanceOf[T15], args.productElement(15).asInstanceOf[T16], args.productElement(16).asInstanceOf[T17], args.productElement(17).asInstanceOf[T18], args.productElement(18).asInstanceOf[T19])
  }
}

class FunctionAdapter20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => R) extends Function1[Product, R] {

  def apply(args: Product) = {
    assert(args.productArity == 20)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2], args.productElement(2).asInstanceOf[T3], args.productElement(3).asInstanceOf[T4], args.productElement(4).asInstanceOf[T5], args.productElement(5).asInstanceOf[T6], args.productElement(6).asInstanceOf[T7], args.productElement(7).asInstanceOf[T8], args.productElement(8).asInstanceOf[T9], args.productElement(9).asInstanceOf[T10], args.productElement(10).asInstanceOf[T11], args.productElement(11).asInstanceOf[T12], args.productElement(12).asInstanceOf[T13], args.productElement(13).asInstanceOf[T14], args.productElement(14).asInstanceOf[T15], args.productElement(15).asInstanceOf[T16], args.productElement(16).asInstanceOf[T17], args.productElement(17).asInstanceOf[T18], args.productElement(18).asInstanceOf[T19], args.productElement(19).asInstanceOf[T20])
  }
}

class FunctionAdapter21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => R) extends Function1[Product, R] {

  def apply(args: Product) = {
    assert(args.productArity == 21)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2], args.productElement(2).asInstanceOf[T3], args.productElement(3).asInstanceOf[T4], args.productElement(4).asInstanceOf[T5], args.productElement(5).asInstanceOf[T6], args.productElement(6).asInstanceOf[T7], args.productElement(7).asInstanceOf[T8], args.productElement(8).asInstanceOf[T9], args.productElement(9).asInstanceOf[T10], args.productElement(10).asInstanceOf[T11], args.productElement(11).asInstanceOf[T12], args.productElement(12).asInstanceOf[T13], args.productElement(13).asInstanceOf[T14], args.productElement(14).asInstanceOf[T15], args.productElement(15).asInstanceOf[T16], args.productElement(16).asInstanceOf[T17], args.productElement(17).asInstanceOf[T18], args.productElement(18).asInstanceOf[T19], args.productElement(19).asInstanceOf[T20], args.productElement(20).asInstanceOf[T21])
  }
}

class FunctionAdapter22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => R) extends Function1[Product, R] {

  def apply(args: Product) = {
    assert(args.productArity == 22)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2], args.productElement(2).asInstanceOf[T3], args.productElement(3).asInstanceOf[T4], args.productElement(4).asInstanceOf[T5], args.productElement(5).asInstanceOf[T6], args.productElement(6).asInstanceOf[T7], args.productElement(7).asInstanceOf[T8], args.productElement(8).asInstanceOf[T9], args.productElement(9).asInstanceOf[T10], args.productElement(10).asInstanceOf[T11], args.productElement(11).asInstanceOf[T12], args.productElement(12).asInstanceOf[T13], args.productElement(13).asInstanceOf[T14], args.productElement(14).asInstanceOf[T15], args.productElement(15).asInstanceOf[T16], args.productElement(16).asInstanceOf[T17], args.productElement(17).asInstanceOf[T18], args.productElement(18).asInstanceOf[T19], args.productElement(19).asInstanceOf[T20], args.productElement(20).asInstanceOf[T21], args.productElement(21).asInstanceOf[T22])
  }
}