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
  
  private[borachio] def satisfied = expected match {
    case Some(n) => actual == n
    case None => actual > 0
  }
  
  private[borachio] def exhausted = expected match {
    case Some(n) => actual == n
    case None => false
  }
  
  private[borachio] def canHandle(mock: MockFunction) = mock == target
  
  private[borachio] def handle() = returnValue.getOrElse(null)

  private var expectedArguments: Option[Product] = None
  private var returnValue: Option[Any] = None
  private var expected: Option[Int] = None

  private var actual = 0
}
