package com.borachio

class Expectation(target: MockFunction) {
  
  def expects(arguments: Product = None) = {
    expectedArguments = Some(arguments)
    this
  }
  
  def returns(value: Any) = {
    returnValue = Some(value)
    this
  }
  
  def returning(value: Any) = returns(value)
  
  private[borachio] def satisfied = expected match {
    case Some(n) => actual == n
    case None => actual > 0
  }
  
  private[borachio] def exhausted = expected match {
    case Some(n) => actual == n
    case None => false
  }
  
  private[borachio] def handle(mock: MockFunction, arguments: Product): Option[Any] = {
    if (mock == target) {
      if (!expectedArguments.isDefined || expectedArguments.get == arguments) {
        actual += 1
        return Some(returnValue.getOrElse(null))
      }
    }
    None
  }

  private var expectedArguments: Option[Product] = None
  private var returnValue: Option[Any] = None
  private var expected: Option[Int] = None

  private var actual = 0
}
