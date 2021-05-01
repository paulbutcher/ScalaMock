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

import org.scalamock.context.{MockContext, Call}
import org.scalamock.handlers._
import org.scalamock.matchers.MockParameter
import org.scalamock.util.Defaultable

class StubFunction0[R: Defaultable](mockContext: MockContext, name: Symbol)
  extends FakeFunction0[R](mockContext, name) {
  
  def when() = mockContext.add((new CallHandler0[R](this)).anyNumberOfTimes())
  def when(matcher: FunctionAdapter0[Boolean]) = mockContext.add((new CallHandler0[R](this, matcher)).anyNumberOfTimes())
  
  def verify() = mockContext.add(new CallHandler0[R](this) with Verify)
  def verify(matcher: FunctionAdapter0[Boolean]) = mockContext.add(new CallHandler0[R](this, matcher) with Verify)
  
  protected def onUnexpected(call: Call) = implicitly[Defaultable[R]].default
}

class StubFunction1[T1, R: Defaultable](mockContext: MockContext, name: Symbol)
  extends FakeFunction1[T1, R](mockContext, name) {

  def when(v1: MockParameter[T1]) = mockContext.add((new CallHandler1[T1, R](this, v1)).anyNumberOfTimes())
  def when(matcher: FunctionAdapter1[T1, Boolean]) = mockContext.add((new CallHandler1[T1, R](this, matcher)).anyNumberOfTimes())

  def verify(v1: MockParameter[T1]) = mockContext.add(new CallHandler1[T1, R](this, v1) with Verify)
  def verify(matcher: FunctionAdapter1[T1, Boolean]) = mockContext.add(new CallHandler1[T1, R](this, matcher) with Verify)
  
  protected def onUnexpected(call: Call) = implicitly[Defaultable[R]].default
}

class StubFunction2[T1, T2, R: Defaultable](mockContext: MockContext, name: Symbol)
  extends FakeFunction2[T1, T2, R](mockContext, name) {

  def when(v1: MockParameter[T1], v2: MockParameter[T2]) = mockContext.add((new CallHandler2[T1, T2, R](this, v1, v2)).anyNumberOfTimes())
  def when(matcher: FunctionAdapter2[T1, T2, Boolean]) = mockContext.add((new CallHandler2[T1, T2, R](this, matcher)).anyNumberOfTimes())

  def verify(v1: MockParameter[T1], v2: MockParameter[T2]) = mockContext.add(new CallHandler2[T1, T2, R](this, v1, v2) with Verify)
  def verify(matcher: FunctionAdapter2[T1, T2, Boolean]) = mockContext.add(new CallHandler2[T1, T2, R](this, matcher) with Verify)
  
  protected def onUnexpected(call: Call) = implicitly[Defaultable[R]].default
}

class StubFunction3[T1, T2, T3, R: Defaultable](mockContext: MockContext, name: Symbol)
  extends FakeFunction3[T1, T2, T3, R](mockContext, name) {

  def when(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3]) = mockContext.add((new CallHandler3[T1, T2, T3, R](this, v1, v2, v3)).anyNumberOfTimes())
  def when(matcher: FunctionAdapter3[T1, T2, T3, Boolean]) = mockContext.add((new CallHandler3[T1, T2, T3, R](this, matcher)).anyNumberOfTimes())

  def verify(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3]) = mockContext.add(new CallHandler3[T1, T2, T3, R](this, v1, v2, v3) with Verify)
  def verify(matcher: FunctionAdapter3[T1, T2, T3, Boolean]) = mockContext.add(new CallHandler3[T1, T2, T3, R](this, matcher) with Verify)
  
  protected def onUnexpected(call: Call) = implicitly[Defaultable[R]].default
}

class StubFunction4[T1, T2, T3, T4, R: Defaultable](mockContext: MockContext, name: Symbol)
  extends FakeFunction4[T1, T2, T3, T4, R](mockContext, name) {

  def when(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4]) = mockContext.add((new CallHandler4[T1, T2, T3, T4, R](this, v1, v2, v3, v4)).anyNumberOfTimes())
  def when(matcher: FunctionAdapter4[T1, T2, T3, T4, Boolean]) = mockContext.add((new CallHandler4[T1, T2, T3, T4, R](this, matcher)).anyNumberOfTimes())

  def verify(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4]) = mockContext.add(new CallHandler4[T1, T2, T3, T4, R](this, v1, v2, v3, v4) with Verify)
  def verify(matcher: FunctionAdapter4[T1, T2, T3, T4, Boolean]) = mockContext.add(new CallHandler4[T1, T2, T3, T4, R](this, matcher) with Verify)
  
  protected def onUnexpected(call: Call) = implicitly[Defaultable[R]].default
}

class StubFunction5[T1, T2, T3, T4, T5, R: Defaultable](mockContext: MockContext, name: Symbol)
  extends FakeFunction5[T1, T2, T3, T4, T5, R](mockContext, name) {

  def when(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5]) = mockContext.add((new CallHandler5[T1, T2, T3, T4, T5, R](this, v1, v2, v3, v4, v5)).anyNumberOfTimes())
  def when(matcher: FunctionAdapter5[T1, T2, T3, T4, T5, Boolean]) = mockContext.add((new CallHandler5[T1, T2, T3, T4, T5, R](this, matcher)).anyNumberOfTimes())

  def verify(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5]) = mockContext.add(new CallHandler5[T1, T2, T3, T4, T5, R](this, v1, v2, v3, v4, v5) with Verify)
  def verify(matcher: FunctionAdapter5[T1, T2, T3, T4, T5, Boolean]) = mockContext.add(new CallHandler5[T1, T2, T3, T4, T5, R](this, matcher) with Verify)
  
  protected def onUnexpected(call: Call) = implicitly[Defaultable[R]].default
}

class StubFunction6[T1, T2, T3, T4, T5, T6, R: Defaultable](mockContext: MockContext, name: Symbol)
  extends FakeFunction6[T1, T2, T3, T4, T5, T6, R](mockContext, name) {

  def when(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6]) = mockContext.add((new CallHandler6[T1, T2, T3, T4, T5, T6, R](this, v1, v2, v3, v4, v5, v6)).anyNumberOfTimes())
  def when(matcher: FunctionAdapter6[T1, T2, T3, T4, T5, T6, Boolean]) = mockContext.add((new CallHandler6[T1, T2, T3, T4, T5, T6, R](this, matcher)).anyNumberOfTimes())

  def verify(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6]) = mockContext.add(new CallHandler6[T1, T2, T3, T4, T5, T6, R](this, v1, v2, v3, v4, v5, v6) with Verify)
  def verify(matcher: FunctionAdapter6[T1, T2, T3, T4, T5, T6, Boolean]) = mockContext.add(new CallHandler6[T1, T2, T3, T4, T5, T6, R](this, matcher) with Verify)
  
  protected def onUnexpected(call: Call) = implicitly[Defaultable[R]].default
}

class StubFunction7[T1, T2, T3, T4, T5, T6, T7, R: Defaultable](mockContext: MockContext, name: Symbol)
  extends FakeFunction7[T1, T2, T3, T4, T5, T6, T7, R](mockContext, name) {

  def when(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7]) = mockContext.add((new CallHandler7[T1, T2, T3, T4, T5, T6, T7, R](this, v1, v2, v3, v4, v5, v6, v7)).anyNumberOfTimes())
  def when(matcher: FunctionAdapter7[T1, T2, T3, T4, T5, T6, T7, Boolean]) = mockContext.add((new CallHandler7[T1, T2, T3, T4, T5, T6, T7, R](this, matcher)).anyNumberOfTimes())

  def verify(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7]) = mockContext.add(new CallHandler7[T1, T2, T3, T4, T5, T6, T7, R](this, v1, v2, v3, v4, v5, v6, v7) with Verify)
  def verify(matcher: FunctionAdapter7[T1, T2, T3, T4, T5, T6, T7, Boolean]) = mockContext.add(new CallHandler7[T1, T2, T3, T4, T5, T6, T7, R](this, matcher) with Verify)
  
  protected def onUnexpected(call: Call) = implicitly[Defaultable[R]].default
}

class StubFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R: Defaultable](mockContext: MockContext, name: Symbol)
  extends FakeFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](mockContext, name) {

  def when(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8]) = mockContext.add((new CallHandler8[T1, T2, T3, T4, T5, T6, T7, T8, R](this, v1, v2, v3, v4, v5, v6, v7, v8)).anyNumberOfTimes())
  def when(matcher: FunctionAdapter8[T1, T2, T3, T4, T5, T6, T7, T8, Boolean]) = mockContext.add((new CallHandler8[T1, T2, T3, T4, T5, T6, T7, T8, R](this, matcher)).anyNumberOfTimes())

  def verify(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8]) = mockContext.add(new CallHandler8[T1, T2, T3, T4, T5, T6, T7, T8, R](this, v1, v2, v3, v4, v5, v6, v7, v8) with Verify)
  def verify(matcher: FunctionAdapter8[T1, T2, T3, T4, T5, T6, T7, T8, Boolean]) = mockContext.add(new CallHandler8[T1, T2, T3, T4, T5, T6, T7, T8, R](this, matcher) with Verify)
  
  protected def onUnexpected(call: Call) = implicitly[Defaultable[R]].default
}

class StubFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R: Defaultable](mockContext: MockContext, name: Symbol)
  extends FakeFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](mockContext, name) {

  def when(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9]) = mockContext.add((new CallHandler9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9)).anyNumberOfTimes())
  def when(matcher: FunctionAdapter9[T1, T2, T3, T4, T5, T6, T7, T8, T9, Boolean]) = mockContext.add((new CallHandler9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](this, matcher)).anyNumberOfTimes())

  def verify(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9]) = mockContext.add(new CallHandler9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9) with Verify)
  def verify(matcher: FunctionAdapter9[T1, T2, T3, T4, T5, T6, T7, T8, T9, Boolean]) = mockContext.add(new CallHandler9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](this, matcher) with Verify)
  
  protected def onUnexpected(call: Call) = implicitly[Defaultable[R]].default
}

class StubFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R: Defaultable](mockContext: MockContext, name: Symbol)
  extends FakeFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](mockContext, name) {

  def when(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10]) = mockContext.add((new CallHandler10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10)).anyNumberOfTimes())
  def when(matcher: FunctionAdapter10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Boolean]) = mockContext.add((new CallHandler10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](this, matcher)).anyNumberOfTimes())

  def verify(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10]) = mockContext.add(new CallHandler10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10) with Verify)
  def verify(matcher: FunctionAdapter10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Boolean]) = mockContext.add(new CallHandler10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](this, matcher) with Verify)

  protected def onUnexpected(call: Call) = implicitly[Defaultable[R]].default
}

class StubFunction11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R: Defaultable](mockContext: MockContext, name: Symbol)
  extends FakeFunction11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R](mockContext, name) {

  def when(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11]) = mockContext.add((new CallHandler11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11)).anyNumberOfTimes())
  def when(matcher: FunctionAdapter11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Boolean]) = mockContext.add((new CallHandler11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R](this, matcher)).anyNumberOfTimes())

  def verify(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11]) = mockContext.add(new CallHandler11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11) with Verify)
  def verify(matcher: FunctionAdapter11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Boolean]) = mockContext.add(new CallHandler11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R](this, matcher) with Verify)

  protected def onUnexpected(call: Call) = implicitly[Defaultable[R]].default
}

class StubFunction12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R: Defaultable](mockContext: MockContext, name: Symbol)
  extends FakeFunction12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R](mockContext, name) {

  def when(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12]) = mockContext.add((new CallHandler12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12)).anyNumberOfTimes())
  def when(matcher: FunctionAdapter12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, Boolean]) = mockContext.add((new CallHandler12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R](this, matcher)).anyNumberOfTimes())

  def verify(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12]) = mockContext.add(new CallHandler12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12) with Verify)
  def verify(matcher: FunctionAdapter12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, Boolean]) = mockContext.add(new CallHandler12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R](this, matcher) with Verify)

  protected def onUnexpected(call: Call) = implicitly[Defaultable[R]].default
}

class StubFunction13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R: Defaultable](mockContext: MockContext, name: Symbol)
  extends FakeFunction13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R](mockContext, name) {

  def when(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13]) = mockContext.add((new CallHandler13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13)).anyNumberOfTimes())
  def when(matcher: FunctionAdapter13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, Boolean]) = mockContext.add((new CallHandler13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R](this, matcher)).anyNumberOfTimes())

  def verify(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13]) = mockContext.add(new CallHandler13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13) with Verify)
  def verify(matcher: FunctionAdapter13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, Boolean]) = mockContext.add(new CallHandler13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R](this, matcher) with Verify)

  protected def onUnexpected(call: Call) = implicitly[Defaultable[R]].default
}

class StubFunction14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R: Defaultable](mockContext: MockContext, name: Symbol)
  extends FakeFunction14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R](mockContext, name) {

  def when(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14]) = mockContext.add((new CallHandler14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14)).anyNumberOfTimes())
  def when(matcher: FunctionAdapter14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, Boolean]) = mockContext.add((new CallHandler14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R](this, matcher)).anyNumberOfTimes())

  def verify(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14]) = mockContext.add(new CallHandler14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14) with Verify)
  def verify(matcher: FunctionAdapter14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, Boolean]) = mockContext.add(new CallHandler14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R](this, matcher) with Verify)

  protected def onUnexpected(call: Call) = implicitly[Defaultable[R]].default
}

class StubFunction15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R: Defaultable](mockContext: MockContext, name: Symbol)
  extends FakeFunction15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R](mockContext, name) {

  def when(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15]) = mockContext.add((new CallHandler15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15)).anyNumberOfTimes())
  def when(matcher: FunctionAdapter15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, Boolean]) = mockContext.add((new CallHandler15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R](this, matcher)).anyNumberOfTimes())

  def verify(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15]) = mockContext.add(new CallHandler15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15) with Verify)
  def verify(matcher: FunctionAdapter15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, Boolean]) = mockContext.add(new CallHandler15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R](this, matcher) with Verify)

  protected def onUnexpected(call: Call) = implicitly[Defaultable[R]].default
}

class StubFunction16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R: Defaultable](mockContext: MockContext, name: Symbol)
  extends FakeFunction16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R](mockContext, name) {

  def when(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15], v16: MockParameter[T16]) = mockContext.add((new CallHandler16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16)).anyNumberOfTimes())
  def when(matcher: FunctionAdapter16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, Boolean]) = mockContext.add((new CallHandler16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R](this, matcher)).anyNumberOfTimes())

  def verify(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15], v16: MockParameter[T16]) = mockContext.add(new CallHandler16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16) with Verify)
  def verify(matcher: FunctionAdapter16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, Boolean]) = mockContext.add(new CallHandler16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R](this, matcher) with Verify)

  protected def onUnexpected(call: Call) = implicitly[Defaultable[R]].default
}

class StubFunction17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R: Defaultable](mockContext: MockContext, name: Symbol)
  extends FakeFunction17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R](mockContext, name) {

  def when(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15], v16: MockParameter[T16], v17: MockParameter[T17]) = mockContext.add((new CallHandler17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17)).anyNumberOfTimes())
  def when(matcher: FunctionAdapter17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, Boolean]) = mockContext.add((new CallHandler17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R](this, matcher)).anyNumberOfTimes())

  def verify(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15], v16: MockParameter[T16], v17: MockParameter[T17]) = mockContext.add(new CallHandler17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17) with Verify)
  def verify(matcher: FunctionAdapter17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, Boolean]) = mockContext.add(new CallHandler17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R](this, matcher) with Verify)

  protected def onUnexpected(call: Call) = implicitly[Defaultable[R]].default
}

class StubFunction18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R: Defaultable](mockContext: MockContext, name: Symbol)
  extends FakeFunction18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R](mockContext, name) {

  def when(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15], v16: MockParameter[T16], v17: MockParameter[T17], v18: MockParameter[T18]) = mockContext.add((new CallHandler18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18)).anyNumberOfTimes())
  def when(matcher: FunctionAdapter18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, Boolean]) = mockContext.add((new CallHandler18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R](this, matcher)).anyNumberOfTimes())

  def verify(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15], v16: MockParameter[T16], v17: MockParameter[T17], v18: MockParameter[T18]) = mockContext.add(new CallHandler18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18) with Verify)
  def verify(matcher: FunctionAdapter18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, Boolean]) = mockContext.add(new CallHandler18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R](this, matcher) with Verify)

  protected def onUnexpected(call: Call) = implicitly[Defaultable[R]].default
}

class StubFunction19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R: Defaultable](mockContext: MockContext, name: Symbol)
  extends FakeFunction19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R](mockContext, name) {

  def when(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15], v16: MockParameter[T16], v17: MockParameter[T17], v18: MockParameter[T18], v19: MockParameter[T19]) = mockContext.add((new CallHandler19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19)).anyNumberOfTimes())
  def when(matcher: FunctionAdapter19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, Boolean]) = mockContext.add((new CallHandler19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R](this, matcher)).anyNumberOfTimes())

  def verify(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15], v16: MockParameter[T16], v17: MockParameter[T17], v18: MockParameter[T18], v19: MockParameter[T19]) = mockContext.add(new CallHandler19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19) with Verify)
  def verify(matcher: FunctionAdapter19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, Boolean]) = mockContext.add(new CallHandler19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R](this, matcher) with Verify)

  protected def onUnexpected(call: Call) = implicitly[Defaultable[R]].default
}

class StubFunction20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R: Defaultable](mockContext: MockContext, name: Symbol)
  extends FakeFunction20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R](mockContext, name) {

  def when(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15], v16: MockParameter[T16], v17: MockParameter[T17], v18: MockParameter[T18], v19: MockParameter[T19], v20: MockParameter[T20]) = mockContext.add((new CallHandler20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20)).anyNumberOfTimes())
  def when(matcher: FunctionAdapter20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, Boolean]) = mockContext.add((new CallHandler20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R](this, matcher)).anyNumberOfTimes())

  def verify(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15], v16: MockParameter[T16], v17: MockParameter[T17], v18: MockParameter[T18], v19: MockParameter[T19], v20: MockParameter[T20]) = mockContext.add(new CallHandler20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20) with Verify)
  def verify(matcher: FunctionAdapter20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, Boolean]) = mockContext.add(new CallHandler20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R](this, matcher) with Verify)

  protected def onUnexpected(call: Call) = implicitly[Defaultable[R]].default
}

class StubFunction21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R: Defaultable](mockContext: MockContext, name: Symbol)
  extends FakeFunction21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R](mockContext, name) {

  def when(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15], v16: MockParameter[T16], v17: MockParameter[T17], v18: MockParameter[T18], v19: MockParameter[T19], v20: MockParameter[T20], v21: MockParameter[T21]) = mockContext.add((new CallHandler21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21)).anyNumberOfTimes())
  def when(matcher: FunctionAdapter21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, Boolean]) = mockContext.add((new CallHandler21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R](this, matcher)).anyNumberOfTimes())

  def verify(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15], v16: MockParameter[T16], v17: MockParameter[T17], v18: MockParameter[T18], v19: MockParameter[T19], v20: MockParameter[T20], v21: MockParameter[T21]) = mockContext.add(new CallHandler21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21) with Verify)
  def verify(matcher: FunctionAdapter21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, Boolean]) = mockContext.add(new CallHandler21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R](this, matcher) with Verify)

  protected def onUnexpected(call: Call) = implicitly[Defaultable[R]].default
}

class StubFunction22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R: Defaultable](mockContext: MockContext, name: Symbol)
  extends FakeFunction22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R](mockContext, name) {

  def when(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15], v16: MockParameter[T16], v17: MockParameter[T17], v18: MockParameter[T18], v19: MockParameter[T19], v20: MockParameter[T20], v21: MockParameter[T21], v22: MockParameter[T22]) = mockContext.add((new CallHandler22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22)).anyNumberOfTimes())
  def when(matcher: FunctionAdapter22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, Boolean]) = mockContext.add((new CallHandler22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R](this, matcher)).anyNumberOfTimes())

  def verify(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15], v16: MockParameter[T16], v17: MockParameter[T17], v18: MockParameter[T18], v19: MockParameter[T19], v20: MockParameter[T20], v21: MockParameter[T21], v22: MockParameter[T22]) = mockContext.add(new CallHandler22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R](this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22) with Verify)
  def verify(matcher: FunctionAdapter22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, Boolean]) = mockContext.add(new CallHandler22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R](this, matcher) with Verify)

  protected def onUnexpected(call: Call) = implicitly[Defaultable[R]].default
}