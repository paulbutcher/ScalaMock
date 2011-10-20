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

import scala.collection.mutable.ListBuffer

trait GeneratedMockFactoryBase { self: MockFactoryBase =>

  protected def mock[T: ClassManifest] = {
    val constructor = classToCreate[T].getConstructor(classOf[MockConstructorDummy])
    constructor.newInstance(new MockConstructorDummy).asInstanceOf[T]
  }
  
  protected def objectToMock[M](x: AnyRef) = {
    x.getClass.getMethod("enableForwarding$Mocks").invoke(x)
    mockedObjects += x
    x.asInstanceOf[M]
  }
  
  protected override def resetMocks() {
    mockedObjects foreach { m =>
      m.getClass.getMethod("resetForwarding$Mocks").invoke(m)
    }
    mockedObjects.clear
  }
  
  private def classToCreate[T: ClassManifest] = {
    val erasure = classManifest[T].erasure
    val clazz = Class.forName(erasure.getName)
    if (clazz.isInterface)
      Class.forName(erasure.getPackage.getName +".Mock$"+ erasure.getSimpleName)
    else
      clazz
  }
  
  private val mockedObjects = new ListBuffer[AnyRef]
}