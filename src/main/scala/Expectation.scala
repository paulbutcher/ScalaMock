package com.borachio

class Expectation(target: MockFunction) {
  
  def expects(arguments: Product = None) = {
    require(!expectedArguments.isDefined, "arguments can only be set once")
    expectedArguments = Some(arguments)
    this
  }
  
  // Special case to handle a single argument
  def expects[T](argument: T): Expectation = expects(new Tuple1(argument))
  
  def returns(value: Any) = {
    require(!returnValue.isDefined, "return value can only be set once")
    returnValue = Some(value)
    this
  }
  
  def returning(value: Any) = returns(value)
  
  def repeat(range: Range) = {
    require(!expectedCalls.isDefined, "expected number of calls can only be set once")
    expectedCalls = Some(range)
    this
  }
  
  def repeat(count: Int): Expectation = repeat(count to count)
  
  def never = repeat(0)
  def once = repeat(1)
  def twice = repeat(2)
  
  def anyNumberOfTimes = repeat(0 to scala.Int.MaxValue)
  def atLeastOnce = repeat(1 to scala.Int.MaxValue)
  def atLeastTwice = repeat(2 to scala.Int.MaxValue)

  def noMoreThanOnce = repeat(0 to 1)
  def noMoreThanTwice = repeat(0 to 2)
  
  private[borachio] def satisfied = expectedCalls match {
    case Some(r) => r contains actualCalls
    case None => actualCalls > 0
  }
  
  private[borachio] def exhausted = expectedCalls match {
    case Some(r) => r contains actualCalls
    case None => false
  }
  
  private[borachio] def handle(mock: MockFunction, arguments: Product): Option[Any] = {
    if (mock == target) {
      if (!expectedArguments.isDefined || expectedArguments.get == arguments) {
        actualCalls += 1
        return Some(returnValue.getOrElse(null))
      }
    }
    None
  }

  private var expectedArguments: Option[Product] = None
  private var returnValue: Option[Any] = None
  private var expectedCalls: Option[Range] = None

  private var actualCalls = 0
}
