// Copyright (c) ScalaMock Contributors (https://github.com/paulbutcher/ScalaMock/graphs/contributors)
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

package org.scalamock.proxy

import org.scalamock.context.MockContext

import java.lang.reflect.{InvocationHandler, Proxy as JavaProxy}
import scala.reflect.{ClassTag, classTag}

trait ProxyMockFactory {

  protected def mock[T : ClassTag](implicit mockContext: MockContext) =
    createProxy[T, Mock](new MockInvocationHandler(mockContext))

  protected def stub[T : ClassTag](implicit mockContext: MockContext) =
    createProxy[T, Stub](new StubInvocationHandler(mockContext))

  private def createProxy[T : ClassTag, F : ClassTag](handler: InvocationHandler) = {
    val classLoader = Thread.currentThread.getContextClassLoader
    val interfaces = Array[Class[_]](classTag[T].runtimeClass, classTag[F].runtimeClass)
    try {
      JavaProxy.newProxyInstance(classLoader, interfaces, handler).asInstanceOf[T & F & scala.reflect.Selectable]
    } catch {
      case e: IllegalArgumentException => 
        throw new IllegalArgumentException("Unable to create proxy - possible classloader issue?", e)
    }
  }
}