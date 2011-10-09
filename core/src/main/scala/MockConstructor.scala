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

trait MockConstructor { self: MockFunction =>

  override private[borachio] val failIfUnexpected = false

  override private[borachio] def canHandle(that: MockFunction) = that match {
      case mc: MockConstructor => mockedClass == mc.mockedClass
      case _ => false
    }
  
  protected val mockedClass: Class[_]
}

class MockConstructor0[R: ClassManifest](factory: MockFactoryBase, name: Symbol) 
  extends MockFunction0[R](factory, name) with MockConstructor {
    
  val mockedClass = classManifest[R].erasure
}

class MockConstructor1[T1, R: ClassManifest](factory: MockFactoryBase, name: Symbol) 
  extends MockFunction1[T1, R](factory, name) with MockConstructor {
    
  val mockedClass = classManifest[R].erasure
}

class MockConstructor2[T1, T2, R: ClassManifest](factory: MockFactoryBase, name: Symbol) 
  extends MockFunction2[T1, T2, R](factory, name) with MockConstructor {
    
  val mockedClass = classManifest[R].erasure
}

class MockConstructor3[T1, T2, T3, R: ClassManifest](factory: MockFactoryBase, name: Symbol) 
  extends MockFunction3[T1, T2, T3, R](factory, name) with MockConstructor {
    
  val mockedClass = classManifest[R].erasure
}

class MockConstructor4[T1, T2, T3, T4, R: ClassManifest](factory: MockFactoryBase, name: Symbol) 
  extends MockFunction4[T1, T2, T3, T4, R](factory, name) with MockConstructor {
    
  val mockedClass = classManifest[R].erasure
}

class MockConstructor5[T1, T2, T3, T4, T5, R: ClassManifest](factory: MockFactoryBase, name: Symbol) 
  extends MockFunction5[T1, T2, T3, T4, T5, R](factory, name) with MockConstructor {
    
  val mockedClass = classManifest[R].erasure
}

class MockConstructor6[T1, T2, T3, T4, T5, T6, R: ClassManifest](factory: MockFactoryBase, name: Symbol) 
  extends MockFunction6[T1, T2, T3, T4, T5, T6, R](factory, name) with MockConstructor {
    
  val mockedClass = classManifest[R].erasure
}

class MockConstructor7[T1, T2, T3, T4, T5, T6, T7, R: ClassManifest](factory: MockFactoryBase, name: Symbol) 
  extends MockFunction7[T1, T2, T3, T4, T5, T6, T7, R](factory, name) with MockConstructor {
    
  val mockedClass = classManifest[R].erasure
}

class MockConstructor8[T1, T2, T3, T4, T5, T6, T7, T8, R: ClassManifest](factory: MockFactoryBase, name: Symbol) 
  extends MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](factory, name) with MockConstructor {
    
  val mockedClass = classManifest[R].erasure
}

class MockConstructor9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R: ClassManifest](factory: MockFactoryBase, name: Symbol) 
  extends MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](factory, name) with MockConstructor {
    
  val mockedClass = classManifest[R].erasure
}

class MockConstructor10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R: ClassManifest](factory: MockFactoryBase, name: Symbol) 
  extends MockFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](factory, name) with MockConstructor {
    
  val mockedClass = classManifest[R].erasure
}
