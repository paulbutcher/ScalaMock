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

class MockConstructorDummy

trait MockConstructor[R] extends TypeSafeMockFunction[R] {

  override protected def makeExpectation() = new ConstructorExpectation[R](this)

  override private[borachio] val failIfUnexpected = false
}

class MockConstructor0[R](factory: AbstractMockFactory, name: Symbol) 
  extends MockFunction0[R](factory, name) with MockConstructor[R] {
    
  override def canHandle(that: MockFunction) = that.isInstanceOf[MockConstructor0[R]]
}

class MockConstructor1[T1, R](factory: AbstractMockFactory, name: Symbol) 
  extends MockFunction1[T1, R](factory, name) with MockConstructor[R] {

  override def canHandle(that: MockFunction) = that.isInstanceOf[MockConstructor1[T1, R]]
}

class MockConstructor2[T1, T2, R](factory: AbstractMockFactory, name: Symbol) 
  extends MockFunction2[T1, T2, R](factory, name) with MockConstructor[R] {
  
  override def canHandle(that: MockFunction) = that.isInstanceOf[MockConstructor2[T1, T2, R]]
}

class MockConstructor3[T1, T2, T3, R](factory: AbstractMockFactory, name: Symbol) 
  extends MockFunction3[T1, T2, T3, R](factory, name) with MockConstructor[R] {
  
  override def canHandle(that: MockFunction) = that.isInstanceOf[MockConstructor3[T1, T2, T3, R]]
}

class MockConstructor4[T1, T2, T3, T4, R](factory: AbstractMockFactory, name: Symbol) 
  extends MockFunction4[T1, T2, T3, T4, R](factory, name) with MockConstructor[R] {
  
  override def canHandle(that: MockFunction) = that.isInstanceOf[MockConstructor4[T1, T2, T3, T4, R]]
}

class MockConstructor5[T1, T2, T3, T4, T5, R](factory: AbstractMockFactory, name: Symbol) 
  extends MockFunction5[T1, T2, T3, T4, T5, R](factory, name) with MockConstructor[R] {
  
  override def canHandle(that: MockFunction) = that.isInstanceOf[MockConstructor5[T1, T2, T3, T4, T5, R]]
}

class MockConstructor6[T1, T2, T3, T4, T5, T6, R](factory: AbstractMockFactory, name: Symbol) 
  extends MockFunction6[T1, T2, T3, T4, T5, T6, R](factory, name) with MockConstructor[R] {
  
  override def canHandle(that: MockFunction) = that.isInstanceOf[MockConstructor6[T1, T2, T3, T4, T5, T6, R]]
}

class MockConstructor7[T1, T2, T3, T4, T5, T6, T7, R](factory: AbstractMockFactory, name: Symbol) 
  extends MockFunction7[T1, T2, T3, T4, T5, T6, T7, R](factory, name) with MockConstructor[R] {
  
  override def canHandle(that: MockFunction) = that.isInstanceOf[MockConstructor7[T1, T2, T3, T4, T5, T6, T7, R]]
}

class MockConstructor8[T1, T2, T3, T4, T5, T6, T7, T8, R](factory: AbstractMockFactory, name: Symbol) 
  extends MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](factory, name) with MockConstructor[R] {
  
  override def canHandle(that: MockFunction) = that.isInstanceOf[MockConstructor8[T1, T2, T3, T4, T5, T6, T7, T8, R]]
}

class MockConstructor9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](factory: AbstractMockFactory, name: Symbol) 
  extends MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](factory, name) with MockConstructor[R] {
  
  override def canHandle(that: MockFunction) = that.isInstanceOf[MockConstructor9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]]
}

class MockConstructor10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](factory: AbstractMockFactory, name: Symbol) 
  extends MockFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](factory, name) with MockConstructor[R] {
  
  override def canHandle(that: MockFunction) = that.isInstanceOf[MockConstructor10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R]]
}
