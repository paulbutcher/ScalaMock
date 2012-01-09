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

package org.scalamock

import scala.collection.mutable.{ListBuffer, Map}

trait ProxyMockFactory { self: MockFactoryBase =>
  
  protected def getClassLoaderStrategy = ClassLoaderStrategy.default
  
  protected def mock[T: ClassManifest] = {
    val proxy = Proxy.create(getClassLoaderStrategy, classOf[Mock], classManifest[T].erasure) {
      (proxy: AnyRef, name: Symbol, args: Array[AnyRef]) =>
        try {
          name match {
            case 'expects => addExpectation(proxy, args(0).asInstanceOf[Symbol])
            case 'stubs => addExpectation(proxy, args(0).asInstanceOf[Symbol]).anyNumberOfTimes
            case 'toString => "proxy mock object "+ System.identityHashCode(proxy)
            case _ => methodsFor(proxy)(name)(args).asInstanceOf[AnyRef]
          }
        } catch {
          case e: NoSuchElementException => {
            val argsString = if (args != null)
              " with arguments: "+ args.mkString("(", ", ", ")")
            else
              ""
            self.handleUnexpectedCall(name + argsString)
          }
        }
    }
    proxies += (proxy -> Map[Symbol, ProxyMockFunction]())
    proxy.asInstanceOf[T with Mock]
  }
  
  private def methodsFor(proxy: AnyRef) = (proxies find { _._1 eq proxy }).get._2
  
  private def addExpectation(proxy: AnyRef, method: Symbol) =
    getOrCreate(proxy, method).toExpectation
  
  private def getOrCreate(proxy: AnyRef, name: Symbol) = {
    val methods = methodsFor(proxy)
    if (methods contains name) {
      methods(name)
    } else {
      val m = new ProxyMockFunction(self, name)
      methods += name -> m
      m
    }
  }

  private val proxies = ListBuffer[(AnyRef, Map[Symbol, ProxyMockFunction])]()
}
