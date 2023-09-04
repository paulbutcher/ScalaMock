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

import org.scalamock.context.MockContext
import org.scalamock.util.Defaultable

trait MockFunctions { this: MockContext =>
  import scala.language.implicitConversions

  protected case class FunctionName(name: Symbol)
  protected implicit def functionName(name: Symbol): FunctionName = FunctionName(name)
  protected implicit def functionName(name: String): FunctionName = FunctionName(Symbol(name))

  protected def mockFunction[R: Defaultable](name: FunctionName) = new MockFunction0[R](this, name.name)
  protected def mockFunction[T1, R: Defaultable](name: FunctionName) = new MockFunction1[T1, R](this, name.name)
  protected def mockFunction[T1, T2, R: Defaultable](name: FunctionName) = new MockFunction2[T1, T2, R](this, name.name)
  protected def mockFunction[T1, T2, T3, R: Defaultable](name: FunctionName) = new MockFunction3[T1, T2, T3, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, R: Defaultable](name: FunctionName) = new MockFunction4[T1, T2, T3, T4, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, T5, R: Defaultable](name: FunctionName) = new MockFunction5[T1, T2, T3, T4, T5, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, R: Defaultable](name: FunctionName) = new MockFunction6[T1, T2, T3, T4, T5, T6, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, R: Defaultable](name: FunctionName) = new MockFunction7[T1, T2, T3, T4, T5, T6, T7, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, R: Defaultable](name: FunctionName) = new MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, R: Defaultable](name: FunctionName) = new MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R: Defaultable](name: FunctionName) = new MockFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R: Defaultable](name: FunctionName) = new MockFunction11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R: Defaultable](name: FunctionName) = new MockFunction12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R: Defaultable](name: FunctionName) = new MockFunction13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R: Defaultable](name: FunctionName) = new MockFunction14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R: Defaultable](name: FunctionName) = new MockFunction15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R: Defaultable](name: FunctionName) = new MockFunction16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R: Defaultable](name: FunctionName) = new MockFunction17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R: Defaultable](name: FunctionName) = new MockFunction18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R: Defaultable](name: FunctionName) = new MockFunction19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R: Defaultable](name: FunctionName) = new MockFunction20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R: Defaultable](name: FunctionName) = new MockFunction21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R: Defaultable](name: FunctionName) = new MockFunction22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R](this, name.name)

  protected def mockFunction[R: Defaultable] = new MockFunction0[R](this, generateMockDefaultName("MockFunction0"))
  protected def mockFunction[T1, R: Defaultable] = new MockFunction1[T1, R](this, generateMockDefaultName("MockFunction1"))
  protected def mockFunction[T1, T2, R: Defaultable] = new MockFunction2[T1, T2, R](this, generateMockDefaultName("MockFunction2"))
  protected def mockFunction[T1, T2, T3, R: Defaultable] = new MockFunction3[T1, T2, T3, R](this, generateMockDefaultName("MockFunction3"))
  protected def mockFunction[T1, T2, T3, T4, R: Defaultable] = new MockFunction4[T1, T2, T3, T4, R](this, generateMockDefaultName("MockFunction4"))
  protected def mockFunction[T1, T2, T3, T4, T5, R: Defaultable] = new MockFunction5[T1, T2, T3, T4, T5, R](this, generateMockDefaultName("MockFunction5"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, R: Defaultable] = new MockFunction6[T1, T2, T3, T4, T5, T6, R](this, generateMockDefaultName("MockFunction6"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, R: Defaultable] = new MockFunction7[T1, T2, T3, T4, T5, T6, T7, R](this, generateMockDefaultName("MockFunction7"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, R: Defaultable] = new MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](this, generateMockDefaultName("MockFunction8"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, R: Defaultable] = new MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](this, generateMockDefaultName("MockFunction9"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R: Defaultable] = new MockFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](this, generateMockDefaultName("MockFunction10"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R: Defaultable] = new MockFunction11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R](this, generateMockDefaultName("MockFunction11"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R: Defaultable] = new MockFunction12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R](this, generateMockDefaultName("MockFunction12"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R: Defaultable] = new MockFunction13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R](this, generateMockDefaultName("MockFunction13"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R: Defaultable] = new MockFunction14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R](this, generateMockDefaultName("MockFunction14"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R: Defaultable] = new MockFunction15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R](this, generateMockDefaultName("MockFunction15"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R: Defaultable] = new MockFunction16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R](this, generateMockDefaultName("MockFunction16"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R: Defaultable] = new MockFunction17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R](this, generateMockDefaultName("MockFunction17"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R: Defaultable] = new MockFunction18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R](this, generateMockDefaultName("MockFunction18"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R: Defaultable] = new MockFunction19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R](this, generateMockDefaultName("MockFunction19"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R: Defaultable] = new MockFunction20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R](this, generateMockDefaultName("MockFunction20"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R: Defaultable] = new MockFunction21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R](this, generateMockDefaultName("MockFunction21"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R: Defaultable] = new MockFunction22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R](this, generateMockDefaultName("MockFunction22"))

  protected def stubFunction[R: Defaultable](name: FunctionName) = new StubFunction0[R](this, name.name)
  protected def stubFunction[T1, R: Defaultable](name: FunctionName) = new StubFunction1[T1, R](this, name.name)
  protected def stubFunction[T1, T2, R: Defaultable](name: FunctionName) = new StubFunction2[T1, T2, R](this, name.name)
  protected def stubFunction[T1, T2, T3, R: Defaultable](name: FunctionName) = new StubFunction3[T1, T2, T3, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, R: Defaultable](name: FunctionName) = new StubFunction4[T1, T2, T3, T4, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, T5, R: Defaultable](name: FunctionName) = new StubFunction5[T1, T2, T3, T4, T5, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, T5, T6, R: Defaultable](name: FunctionName) = new StubFunction6[T1, T2, T3, T4, T5, T6, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, R: Defaultable](name: FunctionName) = new StubFunction7[T1, T2, T3, T4, T5, T6, T7, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, R: Defaultable](name: FunctionName) = new StubFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, R: Defaultable](name: FunctionName) = new StubFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R: Defaultable](name: FunctionName) = new StubFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R: Defaultable](name: FunctionName) = new StubFunction11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R: Defaultable](name: FunctionName) = new StubFunction12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R: Defaultable](name: FunctionName) = new StubFunction13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R: Defaultable](name: FunctionName) = new StubFunction14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R: Defaultable](name: FunctionName) = new StubFunction15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R: Defaultable](name: FunctionName) = new StubFunction16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R: Defaultable](name: FunctionName) = new StubFunction17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R: Defaultable](name: FunctionName) = new StubFunction18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R: Defaultable](name: FunctionName) = new StubFunction19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R: Defaultable](name: FunctionName) = new StubFunction20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R: Defaultable](name: FunctionName) = new StubFunction21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R: Defaultable](name: FunctionName) = new StubFunction22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R](this, name.name)

  protected def stubFunction[R: Defaultable] = new StubFunction0[R](this, generateMockDefaultName("StubFunction0"))
  protected def stubFunction[T1, R: Defaultable] = new StubFunction1[T1, R](this, generateMockDefaultName("StubFunction1"))
  protected def stubFunction[T1, T2, R: Defaultable] = new StubFunction2[T1, T2, R](this, generateMockDefaultName("StubFunction2"))
  protected def stubFunction[T1, T2, T3, R: Defaultable] = new StubFunction3[T1, T2, T3, R](this, generateMockDefaultName("StubFunction3"))
  protected def stubFunction[T1, T2, T3, T4, R: Defaultable] = new StubFunction4[T1, T2, T3, T4, R](this, generateMockDefaultName("StubFunction4"))
  protected def stubFunction[T1, T2, T3, T4, T5, R: Defaultable] = new StubFunction5[T1, T2, T3, T4, T5, R](this, generateMockDefaultName("StubFunction5"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, R: Defaultable] = new StubFunction6[T1, T2, T3, T4, T5, T6, R](this, generateMockDefaultName("StubFunction6"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, R: Defaultable] = new StubFunction7[T1, T2, T3, T4, T5, T6, T7, R](this, generateMockDefaultName("StubFunction7"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, R: Defaultable] = new StubFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](this, generateMockDefaultName("StubFunction8"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, R: Defaultable] = new StubFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](this, generateMockDefaultName("StubFunction9"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R: Defaultable] = new StubFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](this, generateMockDefaultName("StubFunction10"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R: Defaultable] = new StubFunction11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R](this, generateMockDefaultName("StubFunction11"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R: Defaultable] = new StubFunction12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R](this, generateMockDefaultName("StubFunction12"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R: Defaultable] = new StubFunction13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R](this, generateMockDefaultName("StubFunction13"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R: Defaultable] = new StubFunction14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R](this, generateMockDefaultName("StubFunction14"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R: Defaultable] = new StubFunction15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R](this, generateMockDefaultName("StubFunction15"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R: Defaultable] = new StubFunction16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R](this, generateMockDefaultName("StubFunction16"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R: Defaultable] = new StubFunction17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R](this, generateMockDefaultName("StubFunction17"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R: Defaultable] = new StubFunction18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R](this, generateMockDefaultName("StubFunction18"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R: Defaultable] = new StubFunction19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R](this, generateMockDefaultName("StubFunction19"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R: Defaultable] = new StubFunction20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R](this, generateMockDefaultName("StubFunction20"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R: Defaultable] = new StubFunction21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R](this, generateMockDefaultName("StubFunction21"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R: Defaultable] = new StubFunction22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R](this, generateMockDefaultName("StubFunction22"))
}
