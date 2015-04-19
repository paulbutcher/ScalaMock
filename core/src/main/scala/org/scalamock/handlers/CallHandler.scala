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
import org.scalamock.function._
import org.scalamock.matchers.{MockParameter, ArgumentMatcher}
import org.scalamock.util.Defaultable

class CallHandler[R: Defaultable](private[scalamock] val target: FakeFunction, private[scalamock] val argumentMatcher: Product => Boolean) extends Handler {
  import org.scalamock.handlers.CallHandler._
  
  type Derived <: CallHandler[R]
  
  def repeat(range: Range) = {
    expectedCalls = range
    this.asInstanceOf[Derived]
  }
  
  def repeat(count: Int): CallHandler[R] = repeat(count to count)
  
  def never() = repeat(NEVER)
  def once() = repeat(ONCE)
  def twice() = repeat(TWICE)
  
  def anyNumberOfTimes() = repeat(ANY_NUMBER_OF_TIMES)
  def atLeastOnce() = repeat(AT_LEAST_ONCE)
  def atLeastTwice() = repeat(AT_LEAST_TWICE)

  def noMoreThanOnce() = repeat(NO_MORE_THAN_ONCE)
  def noMoreThanTwice() = repeat(NO_MORE_THAN_TWICE)
  
  def repeated(range: Range) = repeat(range)
  def repeated(count: Int) = repeat(count)
  def times() = this.asInstanceOf[Derived]

  def returns(value: R) = onCall({_ => value})
  def returning(value: R) = returns(value)
  
  def throws(e: Throwable) = onCall({_ => throw e})
  def throwing(e: Throwable) = throws(e)
  
  def onCall(handler: Product => R) = {
    onCallHandler = handler
    this.asInstanceOf[Derived]
  }
  
  override def toString = {
    val expected = expectedCalls match {
      case NEVER => "never"
      case ONCE => "once"
      case TWICE => "twice"
      case ANY_NUMBER_OF_TIMES => "any number of times"
      case AT_LEAST_ONCE => "at least once"
      case AT_LEAST_TWICE => "at least twice"
      case NO_MORE_THAN_ONCE => "no more than once"
      case NO_MORE_THAN_TWICE => "no more than twice"
      case r if r.size == 1 => s"${r.start} times"
      case r => s"between ${r.start} and ${r.end} times"
    }
    val actual = actualCalls match {
      case 0 => "never called"
      case 1 => "called once"
      case 2 => "called twice"
      case n => s"called $n times" 
    }
    val satisfied = if (isSatisfied) "" else " - UNSATISFIED"
    s"$target$argumentMatcher $expected ($actual$satisfied)"
  }

  private[scalamock] def handle(call: Call) = {
    if (target == call.target && !isExhausted && argumentMatcher(call.arguments)) {
      actualCalls += 1
      Some(onCallHandler(call.arguments))
    } else {
      None
    }
  }
  
  private[scalamock] def verify(call: Call) = false
  
  private[scalamock] def isSatisfied = expectedCalls contains actualCalls
  
  private[scalamock] def isExhausted = expectedCalls.last <= actualCalls
  
  private[scalamock] var expectedCalls: Range = 1 to 1
  private[scalamock] var actualCalls: Int = 0
  private[scalamock] var onCallHandler: Product => R = {_ => implicitly[Defaultable[R]].default }
}

object CallHandler {

  private[scalamock] val NEVER = 0 to 0
  private[scalamock] val ONCE = 1 to 1
  private[scalamock] val TWICE = 2 to 2
  
  private[scalamock] val ANY_NUMBER_OF_TIMES = 0 to scala.Int.MaxValue - 1
  private[scalamock] val AT_LEAST_ONCE = 1 to scala.Int.MaxValue - 1
  private[scalamock] val AT_LEAST_TWICE = 2 to scala.Int.MaxValue - 1

  private[scalamock] val NO_MORE_THAN_ONCE = 0 to 1
  private[scalamock] val NO_MORE_THAN_TWICE = 0 to 2
}

trait Verify { self: CallHandler[_] =>
  
  private[scalamock] override def handle(call: Call) = sys.error("verify should appear after all code under test has been exercised")
  
  private[scalamock] override def verify(call: Call) = {
    if (self.target == call.target && argumentMatcher(call.arguments)) {
      actualCalls += 1
      true
    } else {
      false
    }
  }
}

class CallHandler0[R: Defaultable](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler0[R]
  
  def this(target: FakeFunction) = this(target, new ArgumentMatcher(None))
  
  def onCall(handler: () => R): CallHandler0[R] = super.onCall(new FunctionAdapter0(handler))
}

class CallHandler1[T1, R: Defaultable](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {
  
  type Derived = CallHandler1[T1, R]

  def this(target: FakeFunction, v1: MockParameter[T1]) = this(target, new ArgumentMatcher(new Tuple1(v1)))
  
  def onCall(handler: (T1) => R): CallHandler1[T1, R] = super.onCall(new FunctionAdapter1(handler))
}

class CallHandler2[T1, T2, R: Defaultable](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {
  
  type Derived = CallHandler2[T1, T2, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2]) = this(target, new ArgumentMatcher((v1, v2)))
  
  def onCall(handler: (T1, T2) => R): CallHandler2[T1, T2, R] = super.onCall(new FunctionAdapter2(handler))
}

class CallHandler3[T1, T2, T3, R: Defaultable](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {
  
  type Derived = CallHandler3[T1, T2, T3, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3]) = this(target, new ArgumentMatcher((v1, v2, v3)))
  
  def onCall(handler: (T1, T2, T3) => R): CallHandler3[T1, T2, T3, R] = super.onCall(new FunctionAdapter3(handler))
}

class CallHandler4[T1, T2, T3, T4, R: Defaultable](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {
  
  type Derived = CallHandler4[T1, T2, T3, T4, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4]) = this(target, new ArgumentMatcher((v1, v2, v3, v4)))
  
  def onCall(handler: (T1, T2, T3, T4) => R): CallHandler4[T1, T2, T3, T4, R] = super.onCall(new FunctionAdapter4(handler))
}

class CallHandler5[T1, T2, T3, T4, T5, R: Defaultable](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {
  
  type Derived = CallHandler5[T1, T2, T3, T4, T5, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5]) = this(target, new ArgumentMatcher((v1, v2, v3, v4, v5)))
  
  def onCall(handler: (T1, T2, T3, T4, T5) => R): CallHandler5[T1, T2, T3, T4, T5, R] = super.onCall(new FunctionAdapter5(handler))
}

class CallHandler6[T1, T2, T3, T4, T5, T6, R: Defaultable](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {
  
  type Derived = CallHandler6[T1, T2, T3, T4, T5, T6, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6]) = this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6)))
  
  def onCall(handler: (T1, T2, T3, T4, T5, T6) => R): CallHandler6[T1, T2, T3, T4, T5, T6, R] = super.onCall(new FunctionAdapter6(handler))
}

class CallHandler7[T1, T2, T3, T4, T5, T6, T7, R: Defaultable](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {
  
  type Derived = CallHandler7[T1, T2, T3, T4, T5, T6, T7, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7]) = this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7)))
  
  def onCall(handler: (T1, T2, T3, T4, T5, T6, T7) => R): CallHandler7[T1, T2, T3, T4, T5, T6, T7, R] = super.onCall(new FunctionAdapter7(handler))
}

class CallHandler8[T1, T2, T3, T4, T5, T6, T7, T8, R: Defaultable](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {
  
  type Derived = CallHandler8[T1, T2, T3, T4, T5, T6, T7, T8, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8]) = this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8)))
  
  def onCall(handler: (T1, T2, T3, T4, T5, T6, T7, T8) => R): CallHandler8[T1, T2, T3, T4, T5, T6, T7, T8, R] = super.onCall(new FunctionAdapter8(handler))
}

class CallHandler9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R: Defaultable](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9]) = this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9)))

  def onCall(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => R): CallHandler9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R] = super.onCall(new FunctionAdapter9(handler))
}

class CallHandler10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R: Defaultable](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10]) = this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10)))

  def onCall(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => R): CallHandler10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R] = super.onCall(new FunctionAdapter10(handler))
}

class CallHandler11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R: Defaultable](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11]) = this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11)))

  def onCall(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => R): CallHandler11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R] = super.onCall(new FunctionAdapter11(handler))
}

class CallHandler12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R: Defaultable](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12]) = this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12)))

  def onCall(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => R): CallHandler12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R] = super.onCall(new FunctionAdapter12(handler))
}

class CallHandler13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R: Defaultable](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13]) = this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13)))

  def onCall(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => R): CallHandler13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R] = super.onCall(new FunctionAdapter13(handler))
}

class CallHandler14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R: Defaultable](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14]) = this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14)))

  def onCall(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => R): CallHandler14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R] = super.onCall(new FunctionAdapter14(handler))
}

class CallHandler15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R: Defaultable](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15]) = this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15)))

  def onCall(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => R): CallHandler15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R] = super.onCall(new FunctionAdapter15(handler))
}

class CallHandler16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R: Defaultable](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15], v16: MockParameter[T16]) = this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16)))

  def onCall(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => R): CallHandler16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R] = super.onCall(new FunctionAdapter16(handler))
}

class CallHandler17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R: Defaultable](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15], v16: MockParameter[T16], v17: MockParameter[T17]) = this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17)))

  def onCall(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => R): CallHandler17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R] = super.onCall(new FunctionAdapter17(handler))
}

class CallHandler18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R: Defaultable](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15], v16: MockParameter[T16], v17: MockParameter[T17], v18: MockParameter[T18]) = this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18)))

  def onCall(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => R): CallHandler18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R] = super.onCall(new FunctionAdapter18(handler))
}

class CallHandler19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R: Defaultable](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15], v16: MockParameter[T16], v17: MockParameter[T17], v18: MockParameter[T18], v19: MockParameter[T19]) = this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19)))

  def onCall(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => R): CallHandler19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R] = super.onCall(new FunctionAdapter19(handler))
}

class CallHandler20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R: Defaultable](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15], v16: MockParameter[T16], v17: MockParameter[T17], v18: MockParameter[T18], v19: MockParameter[T19], v20: MockParameter[T20]) = this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20)))

  def onCall(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => R): CallHandler20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R] = super.onCall(new FunctionAdapter20(handler))
}

class CallHandler21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R: Defaultable](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15], v16: MockParameter[T16], v17: MockParameter[T17], v18: MockParameter[T18], v19: MockParameter[T19], v20: MockParameter[T20], v21: MockParameter[T21]) = this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21)))

  def onCall(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => R): CallHandler21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R] = super.onCall(new FunctionAdapter21(handler))
}

class CallHandler22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R: Defaultable](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3], v4: MockParameter[T4], v5: MockParameter[T5], v6: MockParameter[T6], v7: MockParameter[T7], v8: MockParameter[T8], v9: MockParameter[T9], v10: MockParameter[T10], v11: MockParameter[T11], v12: MockParameter[T12], v13: MockParameter[T13], v14: MockParameter[T14], v15: MockParameter[T15], v16: MockParameter[T16], v17: MockParameter[T17], v18: MockParameter[T18], v19: MockParameter[T19], v20: MockParameter[T20], v21: MockParameter[T21], v22: MockParameter[T22]) = this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22)))

  def onCall(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => R): CallHandler22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R] = super.onCall(new FunctionAdapter22(handler))
}