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

private[scalamock] class UnorderedHandlers(logging: Boolean = false) extends Handlers {
  
  def handle(call: Call): Option[Any] = this.synchronized {
    if (logging) println(s"handling unordered call $call")
    for (handler <- handlers) {
      val r = handler.handle(call)
      if (r.isDefined)
        return r
    }
    None
  }
  
  def verify(call: Call): Boolean = this.synchronized {
    if (logging) println(s"verifying unordered call $call")
    for (handler <- handlers) {
      if (handler.verify(call))
        return true
    }
    false
  }
  
  protected val prefix = "inAnyOrder"
}
