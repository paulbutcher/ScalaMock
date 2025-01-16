// Copyright (c) 2011-2025 ScalaMock Contributors (https://github.com/ScalaMock/ScalaMock/graphs/contributors)
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

package org.scalamock.stubs

import java.util.concurrent.atomic.AtomicReference

class CallLog:
  override def toString: String = internal.calledMethods.mkString("\n")

  object internal:
    private val methodsRef: AtomicReference[List[String]] = AtomicReference(Nil)
    private val uniqueIdx: AtomicReference[Int] = AtomicReference(0)
    
    def nextIdx: Int = uniqueIdx.updateAndGet(_ + 1)
    
    def write(methodName: String): Unit = { methodsRef.getAndUpdate(methodName :: _); () }
    
    def clear(): Unit =
      methodsRef.set(Nil)
      uniqueIdx.set(0)
      
    def calledMethods: List[String] = internal.methodsRef.get().reverse

