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

class MockMatcher1[T1](matcher: T1 => Boolean) extends FunctionAdapter1[T1, Boolean](matcher)

class MockMatcher2[T1, T2](matcher: (T1, T2) => Boolean) extends FunctionAdapter2[T1, T2, Boolean](matcher)

class MockMatcher3[T1, T2, T3](matcher: (T1, T2, T3) => Boolean) extends FunctionAdapter3[T1, T2, T3, Boolean](matcher)

class MockMatcher4[T1, T2, T3, T4](matcher: (T1, T2, T3, T4) => Boolean) extends FunctionAdapter4[T1, T2, T3, T4, Boolean](matcher)

class MockMatcher5[T1, T2, T3, T4, T5](matcher: (T1, T2, T3, T4, T5) => Boolean) extends FunctionAdapter5[T1, T2, T3, T4, T5, Boolean](matcher)

class MockMatcher6[T1, T2, T3, T4, T5, T6](matcher: (T1, T2, T3, T4, T5, T6) => Boolean) extends FunctionAdapter6[T1, T2, T3, T4, T5, T6, Boolean](matcher)

class MockMatcher7[T1, T2, T3, T4, T5, T6, T7](matcher: (T1, T2, T3, T4, T5, T6, T7) => Boolean) extends FunctionAdapter7[T1, T2, T3, T4, T5, T6, T7, Boolean](matcher)

class MockMatcher8[T1, T2, T3, T4, T5, T6, T7, T8](matcher: (T1, T2, T3, T4, T5, T6, T7, T8) => Boolean) extends FunctionAdapter8[T1, T2, T3, T4, T5, T6, T7, T8, Boolean](matcher)

class MockMatcher9[T1, T2, T3, T4, T5, T6, T7, T8, T9](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => Boolean) extends FunctionAdapter9[T1, T2, T3, T4, T5, T6, T7, T8, T9, Boolean](matcher)

class MockMatcher10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => Boolean) extends FunctionAdapter10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Boolean](matcher)
