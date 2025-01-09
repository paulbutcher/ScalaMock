// Copyright (c) 2011-2015 ScalaMock Contributors (https://github.com/paulbutcher/ScalaMock/graphs/contributors)
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

import java.lang.reflect.{InvocationHandler, Method}
import scala.collection.mutable.Map

abstract class InvocationHandlerBase[T <: FakeFunction] extends InvocationHandler {

  override def invoke(proxy: AnyRef, method: Method, args: Array[AnyRef]) = {
    val name = Symbol(method.getName)
    lazy val fake = getFake(args(0).asInstanceOf[Symbol])
    (handle(name, fake).getOrElse(name match {
      case Symbol("toString") => s"proxy mock object ${System.identityHashCode(proxy)}"
      case Symbol("hashCode") => System.identityHashCode(proxy)
      case Symbol("equals") => (args(0) eq proxy)
      case _ => getFake(name).handle(args)
    })).asInstanceOf[AnyRef]
  }

  protected def makeFake(name: Symbol): T

  protected def handle(name: Symbol, fake: => T): Option[Any]

  private def getFake(name: Symbol): T = fakes.getOrElseUpdate(name, makeFake(name))

  private val fakes = Map[Symbol, T]()
}