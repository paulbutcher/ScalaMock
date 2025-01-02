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

import org.scalamock.context.MockContext
import org.scalamock.function.NiceToString
import org.scalamock.handlers.CallHandler
import org.scalamock.matchers.{ArgumentMatcher, MockParameter}
import org.scalamock.function

abstract class FakeFunction(mockContext: MockContext, name: Symbol)
    extends function.FakeFunction(mockContext, name) with NiceToString {

  def handle(args: Array[AnyRef]): Any = handle(
    seq2Product(if (args == null) Seq.empty[AnyRef] else args.toIndexedSeq)
  )

  class ExpectationHandler {
    def apply(args: MockParameter[Any]*) =
      mockContext.add(
        makeHandler(
          new ArgumentMatcher(
            seq2Product(if (args == null) Seq.empty[AnyRef] else args.toIndexedSeq)
          )
        )
      )

    def apply(matcher: (Product) => Boolean) = mockContext.add(makeHandler(matcher))

    def makeHandler(matcher: (Product) => Boolean) =
      new CallHandler[Any](FakeFunction.this, matcher)
  }

  private[proxy] def seq2Product(seq: Seq[AnyRef]): Product = seq match {
    case null => None
    case Seq() => None
    case Seq(v1) => Tuple1(v1)
    case Seq(v1, v2) => (v1, v2)
    case Seq(v1, v2, v3) => (v1, v2, v3)
    case Seq(v1, v2, v3, v4) => (v1, v2, v3, v4)
    case Seq(v1, v2, v3, v4, v5) => (v1, v2, v3, v4, v5)
    case Seq(v1, v2, v3, v4, v5, v6) => (v1, v2, v3, v4, v5, v6)
    case Seq(v1, v2, v3, v4, v5, v6, v7) => (v1, v2, v3, v4, v5, v6, v7)
    case Seq(v1, v2, v3, v4, v5, v6, v7, v8) => (v1, v2, v3, v4, v5, v6, v7, v8)
    case Seq(v1, v2, v3, v4, v5, v6, v7, v8, v9) => (v1, v2, v3, v4, v5, v6, v7, v8, v9)
    case Seq(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10) => (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10)
    case Seq(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11) => (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11)
    case Seq(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12) => (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12)
    case Seq(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13) => (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13)
    case Seq(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14) => (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14)
    case Seq(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15) => (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15)
    case Seq(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16) => (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16)
    case Seq(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17) => (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17)
    case Seq(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18) => (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18)
    case Seq(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19) => (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19)
    case Seq(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20) => (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20)
    case Seq(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21) => (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21)
    case Seq(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22) => (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22)
    case _ => sys.error("ScalaMock can't handle methods that take more than 22 parameters")
  }
}