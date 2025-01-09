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

package org.scalamock.function

import org.scalamock.context.{Call, MockContext}

import scala.reflect.NameTransformer

// This has to be a separate trait, not a method in MockFunction, because
// otherwise linearization will choose the MockFunctionN toString
trait NiceToString { self: FakeFunction =>

  override def toString = NameTransformer.decode(name.name.toString)
}

abstract class FakeFunction(protected val mockContext: MockContext, private[scalamock] val name: Symbol) {

  // [issue #25] we must always refer current mockContext vars because
  // they are updated during intitialize/resetExpectations!
  private def callLog = mockContext.callLog
  private def expectationContext = mockContext.expectationContext

  def handle(arguments: Product): Any = {
    if (callLog != null) {
      val call = new Call(this, arguments)
      callLog += call
      expectationContext.handle(call) getOrElse onUnexpected(call)
    } else {
      val msg = "Can't log call to mock object, have expectations been verified already?"
      throw new RuntimeException(msg)
    }
  }
  
  protected def onUnexpected(call: Call): Any
}

abstract class FakeFunction0[R](mockContext: MockContext, name: Symbol)
  extends FakeFunction(mockContext, name) with Function0[R] with NiceToString {

  def apply() = handle(None).asInstanceOf[R]
}

abstract class FakeFunction1[T1, R](mockContext: MockContext, name: Symbol)
  extends FakeFunction(mockContext, name) with Function1[T1, R] with NiceToString {

  def apply(v1: T1) = handle(Tuple1(v1)).asInstanceOf[R]
}

abstract class FakeFunction2[T1, T2, R](mockContext: MockContext, name: Symbol)
  extends FakeFunction(mockContext, name) with Function2[T1, T2, R] with NiceToString {

  def apply(v1: T1, v2: T2) = handle((v1, v2)).asInstanceOf[R]
}

abstract class FakeFunction3[T1, T2, T3, R](mockContext: MockContext, name: Symbol)
  extends FakeFunction(mockContext, name) with Function3[T1, T2, T3, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3) = handle((v1, v2, v3)).asInstanceOf[R]
}

abstract class FakeFunction4[T1, T2, T3, T4, R](mockContext: MockContext, name: Symbol)
  extends FakeFunction(mockContext, name) with Function4[T1, T2, T3, T4, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4) = handle((v1, v2, v3, v4)).asInstanceOf[R]
}

abstract class FakeFunction5[T1, T2, T3, T4, T5, R](mockContext: MockContext, name: Symbol)
  extends FakeFunction(mockContext, name) with Function5[T1, T2, T3, T4, T5, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5) = handle((v1, v2, v3, v4, v5)).asInstanceOf[R]
}

abstract class FakeFunction6[T1, T2, T3, T4, T5, T6, R](mockContext: MockContext, name: Symbol)
  extends FakeFunction(mockContext, name) with Function6[T1, T2, T3, T4, T5, T6, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6) = handle((v1, v2, v3, v4, v5, v6)).asInstanceOf[R]
}

abstract class FakeFunction7[T1, T2, T3, T4, T5, T6, T7, R](mockContext: MockContext, name: Symbol)
  extends FakeFunction(mockContext, name) with Function7[T1, T2, T3, T4, T5, T6, T7, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7) = handle((v1, v2, v3, v4, v5, v6, v7)).asInstanceOf[R]
}

abstract class FakeFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](mockContext: MockContext, name: Symbol)
  extends FakeFunction(mockContext, name) with Function8[T1, T2, T3, T4, T5, T6, T7, T8, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8) = handle((v1, v2, v3, v4, v5, v6, v7, v8)).asInstanceOf[R]
}

abstract class FakeFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](mockContext: MockContext, name: Symbol)
  extends FakeFunction(mockContext, name) with Function9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8, v9: T9) = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9)).asInstanceOf[R]
}

abstract class FakeFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](mockContext: MockContext, name: Symbol)
  extends FakeFunction(mockContext, name) with Function10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8, v9: T9, v10: T10) = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10)).asInstanceOf[R]
}

abstract class FakeFunction11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R](mockContext: MockContext, name: Symbol)
  extends FakeFunction(mockContext, name) with Function11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8, v9: T9, v10: T10, v11: T11) = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11)).asInstanceOf[R]
}

abstract class FakeFunction12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R](mockContext: MockContext, name: Symbol)
  extends FakeFunction(mockContext, name) with Function12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8, v9: T9, v10: T10, v11: T11, v12: T12) = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12)).asInstanceOf[R]
}

abstract class FakeFunction13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R](mockContext: MockContext, name: Symbol)
  extends FakeFunction(mockContext, name) with Function13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8, v9: T9, v10: T10, v11: T11, v12: T12, v13: T13) = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13)).asInstanceOf[R]
}

abstract class FakeFunction14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R](mockContext: MockContext, name: Symbol)
  extends FakeFunction(mockContext, name) with Function14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8, v9: T9, v10: T10, v11: T11, v12: T12, v13: T13, v14: T14) = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14)).asInstanceOf[R]
}

abstract class FakeFunction15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R](mockContext: MockContext, name: Symbol)
  extends FakeFunction(mockContext, name) with Function15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8, v9: T9, v10: T10, v11: T11, v12: T12, v13: T13, v14: T14, v15: T15) = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15)).asInstanceOf[R]
}

abstract class FakeFunction16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R](mockContext: MockContext, name: Symbol)
  extends FakeFunction(mockContext, name) with Function16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8, v9: T9, v10: T10, v11: T11, v12: T12, v13: T13, v14: T14, v15: T15, v16: T16) = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16)).asInstanceOf[R]
}


abstract class FakeFunction17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R](mockContext: MockContext, name: Symbol)
  extends FakeFunction(mockContext, name) with Function17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8, v9: T9, v10: T10, v11: T11, v12: T12, v13: T13, v14: T14, v15: T15, v16: T16, v17: T17) = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17)).asInstanceOf[R]
}

abstract class FakeFunction18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R](mockContext: MockContext, name: Symbol)
  extends FakeFunction(mockContext, name) with Function18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8, v9: T9, v10: T10, v11: T11, v12: T12, v13: T13, v14: T14, v15: T15, v16: T16, v17: T17, v18: T18) = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18)).asInstanceOf[R]
}

abstract class FakeFunction19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R](mockContext: MockContext, name: Symbol)
  extends FakeFunction(mockContext, name) with Function19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8, v9: T9, v10: T10, v11: T11, v12: T12, v13: T13, v14: T14, v15: T15, v16: T16, v17: T17, v18: T18, v19: T19) = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19)).asInstanceOf[R]
}
abstract class FakeFunction20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R](mockContext: MockContext, name: Symbol)
  extends FakeFunction(mockContext, name) with Function20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8, v9: T9, v10: T10, v11: T11, v12: T12, v13: T13, v14: T14, v15: T15, v16: T16, v17: T17, v18: T18, v19: T19, v20: T20) = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20)).asInstanceOf[R]
}
abstract class FakeFunction21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R](mockContext: MockContext, name: Symbol)
  extends FakeFunction(mockContext, name) with Function21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8, v9: T9, v10: T10, v11: T11, v12: T12, v13: T13, v14: T14, v15: T15, v16: T16, v17: T17, v18: T18, v19: T19, v20: T20, v21: T21) = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21)).asInstanceOf[R]
}
abstract class FakeFunction22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R](mockContext: MockContext, name: Symbol)
  extends FakeFunction(mockContext, name) with Function22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R] with NiceToString {

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8, v9: T9, v10: T10, v11: T11, v12: T12, v13: T13, v14: T14, v15: T15, v16: T16, v17: T17, v18: T18, v19: T19, v20: T20, v21: T21, v22: T22) = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22)).asInstanceOf[R]
}
