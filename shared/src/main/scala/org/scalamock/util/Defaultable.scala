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

package org.scalamock.util

// For an explanation of why this is necessary, see:
// https://groups.google.com/d/topic/scala-internals/7fGPoRGfflM/discussion
trait Defaultable[T] {
  val default: T
}

trait LowPriorityDefaultable {
  
  implicit def default[T]: Defaultable[T] = new Defaultable[T] { val default = null.asInstanceOf[T] }
}

object Defaultable extends LowPriorityDefaultable {

  implicit object defaultByte extends Defaultable[Byte] { val default: Byte = 0 }
  implicit object defaultShort extends Defaultable[Short] { val default: Short = 0 }
  implicit object defaultChar extends Defaultable[Char] { val default: Char = 0 }
  implicit object defaultInt extends Defaultable[Int] { val default = 0 }
  implicit object defaultLong extends Defaultable[Long] { val default = 0L }
  implicit object defaultFloat extends Defaultable[Float] { val default = 0.0f }
  implicit object defaultDouble extends Defaultable[Double] { val default = 0.0d }
  implicit object defaultBoolean extends Defaultable[Boolean] { val default = false }
  implicit object defaultUnit extends Defaultable[Unit] { val default = () }
}
