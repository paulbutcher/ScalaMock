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

package org.scalamock.matchers

object ArgCapture {
  trait Capture[T] {
    def value: T
    def value_=(t: T): Unit
  }

  case class CaptureOne[T]() extends Capture[T] {
    private var v: Option[T] = None
    override def value_=(t: T): Unit = this.synchronized { v = Option(t) }
    def value: T = v.get
  }

  case class CaptureAll[T]() extends Capture[T] {
    private var v: Seq[T] = Nil
    override def value_=(t: T): Unit = this.synchronized { v :+= t }
    def values: Seq[T] = v
    def value: T = v.last
  }

  class CaptureMatcher[T](c: Capture[T]) extends MatchAny {
    override def equals(that: Any): Boolean = {
      c.value = that.asInstanceOf[T]
      super.equals(that)
    }
  }

}
