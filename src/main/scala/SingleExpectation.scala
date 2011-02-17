package com.borachio

class SingleExpectation(
    val target: AnyRef,
    expectedArguments: Option[Product] = None, 
    returnValue: Option[Any] = None, 
    count: Option[Int] = None)
  extends Expectation {
  
  override def withArguments(arguments: Product) = {
    require(!expectedArguments.isDefined, "arguments can only be set once")
    new SingleExpectation(target, Some(arguments), returnValue, count)
  }
  
  override def returning(value: Any) = {
    require(!returnValue.isDefined, "return value can only be set once")
    new SingleExpectation(target, expectedArguments, Some(value), count)
  }
  
  override def times(n: Int) = {
    require(!count.isDefined, "count can only be set once")
    new SingleExpectation(target, expectedArguments, returnValue, Some(n))
  }
  
  override def then = new OrderedExpectations(List(this, new SingleExpectation(target)))
  
  override def handle(arguments: Product) = {
    actualCount += 1
    returnValue.getOrElse(null)
  }
  
  override def satisfied = count match {
    case Some(c) => c == actualCount
    case None => actualCount > 0
  }
  
  override def exhausted = count match {
    case Some(c) => c == actualCount
    case None => false
  }
  
  var actualCount = 0
}
