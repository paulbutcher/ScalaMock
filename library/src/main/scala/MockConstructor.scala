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

trait MockConstructor {
  
  var mockObject: Any = _
}

class MockConstructor0[R: ClassManifest](factory: AbstractMockFactory, name: Symbol) 
  extends MockFunction0[R](factory, name) with MockConstructor {
  
  override def toExpectation() = { 
    factory.add(classManifest[R].erasure -> this)
    super.toExpectation
  }
  
  override def apply() = {
    val mock = factory.findMockConstructor(classManifest[R].erasure).asInstanceOf[MockConstructor0[R]]
    mock.handle(Array())
    mock.mockObject.asInstanceOf[R]
  }
}

class MockConstructor1[T1, R: ClassManifest](factory: AbstractMockFactory, name: Symbol) 
  extends MockFunction1[T1, R](factory, name) with MockConstructor {
  
  override def toExpectation() = { 
    factory.add(classManifest[R].erasure -> this)
    super.toExpectation
  }
  
  override def apply(v1: T1) = {
    val mock = factory.findMockConstructor(classManifest[R].erasure).asInstanceOf[MockConstructor1[T1, R]]
    mock.handle(Array(v1))
    mock.mockObject.asInstanceOf[R]
  }
}

class MockConstructor2[T1, T2, R: ClassManifest](factory: AbstractMockFactory, name: Symbol) 
  extends MockFunction2[T1, T2, R](factory, name) with MockConstructor {
  
  override def toExpectation() = { 
    factory.add(classManifest[R].erasure -> this)
    super.toExpectation
  }
  
  override def apply(v1: T1, v2: T2) = {
    val mock = factory.findMockConstructor(classManifest[R].erasure).asInstanceOf[MockConstructor2[T1, T2, R]]
    mock.handle(Array(v1, v2))
    mock.mockObject.asInstanceOf[R]
  }
}
