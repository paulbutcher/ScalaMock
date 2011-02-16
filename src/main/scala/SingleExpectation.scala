package com.borachio

class SingleExpectation(arguments: Option[Product] = None, 
                        returnValue: Option[Any] = None, 
                        count: Option[Int] = None)
  extends Expectation {
  
  override def withArguments(arguments: Product) = {
    require(!this.arguments.isDefined, "arguments can only be set once")
    new SingleExpectation(Some(arguments), returnValue, count)
  }
  
  override def returning(returnValue: Any) = {
    require(!this.returnValue.isDefined, "return value can only be set once")
    new SingleExpectation(arguments, Some(returnValue), count)
  }
  
  override def times(count: Int) = {
    require(!this.count.isDefined, "count can only be set once")
    new SingleExpectation(arguments, returnValue, Some(count))
  }
  
  override def then = new OrderedExpectations(List(this, new SingleExpectation))
  
  override def toString = "arguments: "+ arguments + ", returnValue: "+ returnValue + ", count: "+ count
}
