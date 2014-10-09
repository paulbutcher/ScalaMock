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

trait MockFactoryBase extends Mock {
  import language.implicitConversions
  
  type ExpectationException <: Throwable

  private[scalamock] var callLog: CallLog = _
  private[scalamock] var currentExpectationContext: Handlers = _
  private[scalamock] var expectationContext: Handlers = _

  initializeExpectations
  
  def withExpectations[T](what: => T): T = {
    if (expectationContext == null) {
      // we don't reset expectations for the first test case to allow
      // defining expectations in Suite scope and writing tests in OneInstancePerTest/isolated style 
      initializeExpectations
    }

    try {
      val result = inAnyOrder(what) 
      verifyExpectations()
      result
    } catch {
      case ex : Throwable => 
        // do not verify expectations - just clear them. Throw original exception
        // see issue #72
        clearExpectations() 
        throw ex
    }
  }

  protected def inAnyOrder[T](what: => T): T = {
    inContext(new UnorderedHandlers)(what)
  }

  protected def inSequence[T](what: => T): T = {
    inContext(new OrderedHandlers)(what)
  }
  
  //! TODO - https://issues.scala-lang.org/browse/SI-5831
  implicit val _factory = this
  
  protected case class FunctionName(name: Symbol)
  protected implicit def functionName(name: Symbol) = FunctionName(name)
  protected implicit def functionName(name: String) = FunctionName(Symbol(name))

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

  protected def mockFunction[R: Defaultable] = new MockFunction0[R](this, Symbol("unnamed MockFunction0"))
  protected def mockFunction[T1, R: Defaultable] = new MockFunction1[T1, R](this, Symbol("unnamed MockFunction1"))
  protected def mockFunction[T1, T2, R: Defaultable] = new MockFunction2[T1, T2, R](this, Symbol("unnamed MockFunction2"))
  protected def mockFunction[T1, T2, T3, R: Defaultable] = new MockFunction3[T1, T2, T3, R](this, Symbol("unnamed MockFunction3"))
  protected def mockFunction[T1, T2, T3, T4, R: Defaultable] = new MockFunction4[T1, T2, T3, T4, R](this, Symbol("unnamed MockFunction4"))
  protected def mockFunction[T1, T2, T3, T4, T5, R: Defaultable] = new MockFunction5[T1, T2, T3, T4, T5, R](this, Symbol("unnamed MockFunction5"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, R: Defaultable] = new MockFunction6[T1, T2, T3, T4, T5, T6, R](this, Symbol("unnamed MockFunction6"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, R: Defaultable] = new MockFunction7[T1, T2, T3, T4, T5, T6, T7, R](this, Symbol("unnamed MockFunction7"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, R: Defaultable] = new MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](this, Symbol("unnamed MockFunction8"))
  protected def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, R: Defaultable] = new MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](this, Symbol("unnamed MockFunction9"))

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

  protected def stubFunction[R: Defaultable] = new StubFunction0[R](this, Symbol("unnamed StubFunction0"))
  protected def stubFunction[T1, R: Defaultable] = new StubFunction1[T1, R](this, Symbol("unnamed StubFunction1"))
  protected def stubFunction[T1, T2, R: Defaultable] = new StubFunction2[T1, T2, R](this, Symbol("unnamed StubFunction2"))
  protected def stubFunction[T1, T2, T3, R: Defaultable] = new StubFunction3[T1, T2, T3, R](this, Symbol("unnamed StubFunction3"))
  protected def stubFunction[T1, T2, T3, T4, R: Defaultable] = new StubFunction4[T1, T2, T3, T4, R](this, Symbol("unnamed StubFunction4"))
  protected def stubFunction[T1, T2, T3, T4, T5, R: Defaultable] = new StubFunction5[T1, T2, T3, T4, T5, R](this, Symbol("unnamed StubFunction5"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, R: Defaultable] = new StubFunction6[T1, T2, T3, T4, T5, T6, R](this, Symbol("unnamed StubFunction6"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, R: Defaultable] = new StubFunction7[T1, T2, T3, T4, T5, T6, T7, R](this, Symbol("unnamed StubFunction7"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, R: Defaultable] = new StubFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](this, Symbol("unnamed StubFunction8"))
  protected def stubFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, R: Defaultable] = new StubFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](this, Symbol("unnamed StubFunction9"))

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

  private[scalamock] def add[E <: CallHandler[_]](e: E) = {
    assert(currentExpectationContext != null, "Null expectation context - missing withExpectations?")
    currentExpectationContext.add(e)
    e
  }

  private[scalamock] def reportUnexpectedCall(call: Call) =
    throw newExpectationException(s"Unexpected call: $call\n\n%s".format(errorContext(callLog, expectationContext)), Some(call.target.name))

  private def reportUnsatisfiedExpectation(callLog : CallLog, expectationContext : Handlers) =
    throw newExpectationException(s"Unsatisfied expectation:\n\n%s".format(errorContext(callLog, expectationContext)))

  protected def newExpectationException(message: String, methodName: Option[Symbol] = None): ExpectationException

  private def initializeExpectations() {
    val initialHandlers = new UnorderedHandlers
    callLog = new CallLog

    expectationContext = initialHandlers
    currentExpectationContext = initialHandlers
  }

  private def clearExpectations() : Unit = {
    // to forbid setting expectations after verification is done 
    callLog = null
    expectationContext = null
    currentExpectationContext = null
  }

  private def verifyExpectations() {
    callLog foreach expectationContext.verify _
    
    val oldCallLog = callLog
    val oldExpectationContext = expectationContext

    clearExpectations()

    if (!oldExpectationContext.isSatisfied)
      reportUnsatisfiedExpectation(oldCallLog, oldExpectationContext)
  }

  private def errorContext(callLog : CallLog, expectationContext : Handlers) =
    s"Expected:\n${expectationContext}\n\nActual:\n${callLog}"

  private def inContext[T](context: Handlers)(what: => T): T = {
    currentExpectationContext.add(context)
    val prevContext = currentExpectationContext
    currentExpectationContext = context
    val r = what
    currentExpectationContext = prevContext
    r
  }

}
