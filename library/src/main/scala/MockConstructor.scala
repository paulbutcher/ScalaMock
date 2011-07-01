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

trait MockConstructor

class MockConstructor0[R](factory: AbstractMockFactory, name: Symbol) 
  extends MockFunction0[R](factory, name) with MockConstructor {
    
  override def canHandle(that: MockFunction) = that.isInstanceOf[MockConstructor0[R]]
}

class MockConstructor1[T1, R](factory: AbstractMockFactory, name: Symbol) 
  extends MockFunction1[T1, R](factory, name) with MockConstructor {

  override def canHandle(that: MockFunction) = that.isInstanceOf[MockConstructor1[T1, R]]
}

class MockConstructor2[T1, T2, R](factory: AbstractMockFactory, name: Symbol) 
  extends MockFunction2[T1, T2, R](factory, name) with MockConstructor {
  
  override def canHandle(that: MockFunction) = that.isInstanceOf[MockConstructor2[T1, T2, R]]
}
