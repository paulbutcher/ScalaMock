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

import java.io.{DataInputStream, File}
import java.net.{URL, URLClassLoader}

class MockingClassLoader(mockClassPath: URL) extends ClassLoader {
  
  var factory: Any = _
  
  private val defaultClassLoader = getClass.getClassLoader.asInstanceOf[URLClassLoader]
  
  private class ClassLoaderInternal(urls: Array[URL]) extends URLClassLoader(urls) {
    override def loadClass(name: String): Class[_] = MockingClassLoader.this.loadClass(name)
    def loadClassInternal(name: String) = super.loadClass(name)
    def getFactory = factory
    def loadClassNormal(name: String) = MockingClassLoader.this.loadClassNormal(name)
  }
  private val mockClassLoader = new ClassLoaderInternal(Array(mockClassPath))
  private val normalClassLoader = new ClassLoaderInternal(defaultClassLoader.getURLs)
  
  override def loadClass(name: String): Class[_] =
    if (useDefault(name)) {
      defaultClassLoader.loadClass(name)
    } else {
      try {
        mockClassLoader.loadClassInternal(name)
      } catch {
        case _: ClassNotFoundException => loadClassNormal(name)
      }
    }
  
  def loadClassNormal(name: String) =
    try {
      normalClassLoader.loadClassInternal(name)
    } catch {
      case _: ClassNotFoundException => defaultClassLoader.loadClass(name)
    }
  
  private def useDefault(name: String) =
    name.startsWith("scala.") || name.startsWith("java.") || name.startsWith("org.scalatest.")
}
