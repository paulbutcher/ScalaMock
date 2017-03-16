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

package org.scalamock.handlers

import org.scalamock.context.Call

private[scalamock] class OrderedHandlers(logging: Boolean = false) extends Handlers {
  
  val handleFn = new Function1[Call, Option[Any]] {
    
    def apply(call: Call): Option[Any] = {
      for (i <- currentIndex until handlers.length) {
        val handler = handlers(i)
        val r = handler.handle(call)
        if (r.isDefined) {
          currentIndex = i
          return r
        }
        if (!handler.isSatisfied)
          return None
      }
      None
    }

    var currentIndex = 0
  }
  
  def handle(call: Call) = this.synchronized {
    if (logging) println(s"handling ordered call $call")
    handleFn(call)
  }
  
  val verifyFn = new Function1[Call, Boolean] {
    
    def apply(call: Call): Boolean = {
      for (i <- currentIndex until handlers.length) {
        val handler = handlers(i)
        if (handler.verify(call)) {
          currentIndex = i
          return true
        }
      }
      false
    }

    var currentIndex = 0
  }
 
  def verify(call: Call) = this.synchronized {
    if (logging) println(s"verifying ordered call $call")
    verifyFn(call)
  }

  protected val prefix = "inSequence"
}
