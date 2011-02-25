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

package com.borachio

private[borachio] class ProxyMockFunction(expectations: UnorderedExpectations) extends MockFunction(expectations) {

  def apply(args: Array[AnyRef]) = handle(argsToProduct(args))
  
  private def argsToProduct(args: Array[AnyRef]) = {
    if (args != null) {
      args.length match {
        case  0 => None
        case  1 => new Tuple1(args(0))
        case  2 => (args(0), args(1))
        case  3 => (args(0), args(1), args(2))
        case  4 => (args(0), args(1), args(2), args(3))
        case  5 => (args(0), args(1), args(2), args(3), args(4))
        case  6 => (args(0), args(1), args(2), args(3), args(4), args(5))
        case  7 => (args(0), args(1), args(2), args(3), args(4), args(5), args(6))
        case  8 => (args(0), args(1), args(2), args(3), args(4), args(5), args(6), args(7))
        case  9 => (args(0), args(1), args(2), args(3), args(4), args(5), args(6), args(7), args(8))
        case 10 => (args(0), args(1), args(2), args(3), args(4), args(5), args(6), args(7), args(8), args(9))
        case _ => throw new IllegalArgumentException("ProxyMockFunction can't handle this many arguments")
      }
    } else {
      null
    }
  }
}
