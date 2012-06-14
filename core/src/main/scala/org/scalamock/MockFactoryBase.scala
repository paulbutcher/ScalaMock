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

package org.scalamock

import collection.mutable.ListBuffer

trait MockFactoryBase extends Mock {
  import language.implicitConversions
  
  type ExpectationException <: Exception
  
  protected def withExpectations[T](what: => T): T = {
    resetExpectations
    val r = what
    verifyExpectations
    r
  }
  
  protected def inAnyOrder(what: => Unit) {
    inContext(new UnorderedHandlers)(what)
  }
  
  protected def inSequence(what: => Unit) {
    inContext(new OrderedHandlers)(what)
  }
  
  //! TODO - https://issues.scala-lang.org/browse/SI-5831
  implicit val _factory = this
  
  protected case class FunctionName(name: Symbol)
  protected implicit def functionName(name: Symbol) = FunctionName(name)
  protected implicit def functionName(name: String) = FunctionName(Symbol(name))

  protected def mockFunction[R](name: FunctionName) = new MockFunction0[R](this, name.name)
  protected def mockFunction[T1, R](name: FunctionName) = new MockFunction1[T1, R](this, name.name)
  protected def mockFunction[T1, T2, R](name: FunctionName) = new MockFunction2[T1, T2, R](this, name.name)
  protected def mockFunction[T1, T2, T3, R](name: FunctionName) = new MockFunction3[T1, T2, T3, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, R](name: FunctionName) = new MockFunction4[T1, T2, T3, T4, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, T5, R](name: FunctionName) = new MockFunction5[T1, T2, T3, T4, T5, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, R](name: FunctionName) = new MockFunction6[T1, T2, T3, T4, T5, T6, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, R](name: FunctionName) = new MockFunction7[T1, T2, T3, T4, T5, T6, T7, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, R](name: FunctionName) = new MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](this, name.name)
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](name: FunctionName) = new MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](this, name.name)

  protected def mockFunction[R] = new MockFunction0[R](this, Symbol("unnamed MockFunction0"))
  protected def mockFunction[T1, R] = new MockFunction1[T1, R](this, Symbol("unnamed MockFunction1"))
  protected def mockFunction[T1, T2, R] = new MockFunction2[T1, T2, R](this, Symbol("unnamed MockFunction2"))
  protected def mockFunction[T1, T2, T3, R] = new MockFunction3[T1, T2, T3, R](this, Symbol("unnamed MockFunction3"))
  protected def mockFunction[T1, T2, T3, T4, R] = new MockFunction4[T1, T2, T3, T4, R](this, Symbol("unnamed MockFunction4"))
  protected def mockFunction[T1, T2, T3, T4, T5, R] = new MockFunction5[T1, T2, T3, T4, T5, R](this, Symbol("unnamed MockFunction5"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, R] = new MockFunction6[T1, T2, T3, T4, T5, T6, R](this, Symbol("unnamed MockFunction6"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, R] = new MockFunction7[T1, T2, T3, T4, T5, T6, T7, R](this, Symbol("unnamed MockFunction7"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, R] = new MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](this, Symbol("unnamed MockFunction8"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, R] = new MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](this, Symbol("unnamed MockFunction9"))

  protected def stubFunction[R](name: FunctionName) = new StubFunction0[R](this, name.name)
  protected def stubFunction[T1, R](name: FunctionName) = new StubFunction1[T1, R](this, name.name)
  protected def stubFunction[T1, T2, R](name: FunctionName) = new StubFunction2[T1, T2, R](this, name.name)
  protected def stubFunction[T1, T2, T3, R](name: FunctionName) = new StubFunction3[T1, T2, T3, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, R](name: FunctionName) = new StubFunction4[T1, T2, T3, T4, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, T5, R](name: FunctionName) = new StubFunction5[T1, T2, T3, T4, T5, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, T5, T6, R](name: FunctionName) = new StubFunction6[T1, T2, T3, T4, T5, T6, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, R](name: FunctionName) = new StubFunction7[T1, T2, T3, T4, T5, T6, T7, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, R](name: FunctionName) = new StubFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](this, name.name)
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](name: FunctionName) = new StubFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](this, name.name)

  protected def stubFunction[R] = new StubFunction0[R](this, Symbol("unnamed StubFunction0"))
  protected def stubFunction[T1, R] = new StubFunction1[T1, R](this, Symbol("unnamed StubFunction1"))
  protected def stubFunction[T1, T2, R] = new StubFunction2[T1, T2, R](this, Symbol("unnamed StubFunction2"))
  protected def stubFunction[T1, T2, T3, R] = new StubFunction3[T1, T2, T3, R](this, Symbol("unnamed StubFunction3"))
  protected def stubFunction[T1, T2, T3, T4, R] = new StubFunction4[T1, T2, T3, T4, R](this, Symbol("unnamed StubFunction4"))
  protected def stubFunction[T1, T2, T3, T4, T5, R] = new StubFunction5[T1, T2, T3, T4, T5, R](this, Symbol("unnamed StubFunction5"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, R] = new StubFunction6[T1, T2, T3, T4, T5, T6, R](this, Symbol("unnamed StubFunction6"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, R] = new StubFunction7[T1, T2, T3, T4, T5, T6, T7, R](this, Symbol("unnamed StubFunction7"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, R] = new StubFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](this, Symbol("unnamed StubFunction8"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, R] = new StubFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](this, Symbol("unnamed StubFunction9"))

  protected def where[T1](matcher: T1 => Boolean) = new FunctionAdapter1(matcher)
  protected def where[T1, T2](matcher: (T1, T2) => Boolean) = new FunctionAdapter2(matcher)
  protected def where[T1, T2, T3](matcher: (T1, T2, T3) => Boolean) = new FunctionAdapter3(matcher)
  protected def where[T1, T2, T3, T4](matcher: (T1, T2, T3, T4) => Boolean) = new FunctionAdapter4(matcher)
  protected def where[T1, T2, T3, T4, T5](matcher: (T1, T2, T3, T4, T5) => Boolean) = new FunctionAdapter5(matcher)
  protected def where[T1, T2, T3, T4, T5, T6](matcher: (T1, T2, T3, T4, T5, T6) => Boolean) = new FunctionAdapter6(matcher)
  protected def where[T1, T2, T3, T4, T5, T6, T7](matcher: (T1, T2, T3, T4, T5, T6, T7) => Boolean) = new FunctionAdapter7(matcher)
  protected def where[T1, T2, T3, T4, T5, T6, T7, T8](matcher: (T1, T2, T3, T4, T5, T6, T7, T8) => Boolean) = new FunctionAdapter8(matcher)
  protected def where[T1, T2, T3, T4, T5, T6, T7, T8, T9](matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => Boolean) = new FunctionAdapter9(matcher)

  protected def * = new MatchAny

  protected class EpsilonMatcher(d: Double) {
    def unary_~() = new MatchEpsilon(d)
  }
  protected implicit def doubleToEpsilon(d: Double) = new EpsilonMatcher(d)

  protected implicit def toMockParameter[T](v: T) = new MockParameter(v)

  protected implicit def MatchAnyToMockParameter[T](m: MatchAny) = new MockParameter[T](m)

  protected implicit def MatchEpsilonToMockParameter[T](m: MatchEpsilon) = new EpsilonMockParameter(m)
  
  private[scalamock] def handle(call: Call) = {
    callLog.get += call
    expectationContext.get.handle(call)
  }
  
  private[scalamock] def add[E <: CallHandler[_]](e: E) = {
    assert(expectationContext.get != null, "Null expectationContext - missing withExpectations?")
    expectationContext.get.add(e)
    e
  }
  
  private[scalamock] def reportUnexpectedCall(call: Call) =
    throw newExpectationException(s"Unexpected call: $call\n\n$errorContext", Some(call.target.name))
  
  private def reportUnsatisfiedExpectation() =
    throw newExpectationException(s"Unsatisfied expectation:\n\n$errorContext")
  
  protected def newExpectationException(message: String, methodName: Option[Symbol] = None): ExpectationException
  
  private def resetExpectations() {
    callLog set new CallLog
    expectationContext set new UnorderedHandlers
  }
  
  private def verifyExpectations() {
    callLog.get foreach expectationContext.get.verify _
    if (!expectationContext.get.isSatisfied)
      reportUnsatisfiedExpectation
    
    expectationContext set null
  }
  
  private def errorContext =
    s"Expected:\n${expectationContext.get}\n\nActual:\n${callLog.get}"
  
  private def inContext(context: Handlers)(what: => Unit) {
    expectationContext.get.add(context)
    val prevContext = expectationContext.get
    expectationContext set context
    what
    expectationContext set prevContext
  }
  
  private class CallLog {

    def +=(call: Call) = log += call
    
    def foreach(f: Call => Unit) = log foreach f
    
    override def toString = log mkString("  ", "\n  ", "")
    
    private val log = new ListBuffer[Call]
  }
  
  private val callLog = new InheritableThreadLocal[CallLog]
  
  private val expectationContext = new InheritableThreadLocal[Handlers]
}