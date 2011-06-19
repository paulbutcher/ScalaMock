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

class MockingClassLoader extends ClassLoader {
  
  private val defaultClassLoader = getClass.getClassLoader.asInstanceOf[URLClassLoader]
  
  private class ClassLoaderInternal(urls: Array[URL]) extends URLClassLoader(urls) {
    override def loadClass(name: String): Class[_] = MockingClassLoader.this.loadClass(name)
    def loadClassInternal(name: String) = super.loadClass(name)
  }
  private val mockClassLoader = new ClassLoaderInternal(Array(new URL("file:examples/target/scala_2.9.0/mock-classes/")))
  private val normalClassLoader = new ClassLoaderInternal(defaultClassLoader.getURLs)
  
  override def loadClass(name: String): Class[_] = {
    if (shouldDefer(name)) {
      println("++++ MockingClassLoader, deferring: "+ name)
      defaultClassLoader.loadClass(name)
    } else {
      try {
        println("++++ MockingClassLoader load: "+ name)
        mockClassLoader.loadClassInternal(name)
      } catch {
        case _: ClassNotFoundException => {
          println("++++ mock not found: "+ name)
          try {
            normalClassLoader.loadClassInternal(name)
          } catch {
            case e: ClassNotFoundException => {
              println("++++ class not found: "+ name)
              println("++++ Current directory: "+ new File(".").getCanonicalPath)

              defaultClassLoader.loadClass(name)
            }
          }
        }
      }
    }
  }
  
  private def shouldDefer(name: String) = name.startsWith("scala.") || name.startsWith("org.scalatest.")
}
