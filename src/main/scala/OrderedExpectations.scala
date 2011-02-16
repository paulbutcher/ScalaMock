package com.borachio

class OrderedExpectations(expectations: List[SingleExpectation]) extends Expectation {
  
  override def withArguments(arguments: Product) =
    applyToLast(_.withArguments(arguments))
    
  override def returning(value: Any) =
    applyToLast(_.returning(value))

  override def times(n: Int) =
    applyToLast(_.times(n))
  
  override def then = new OrderedExpectations(expectations :+ new SingleExpectation(expectations.last.target))

  private def applyToLast(what: (SingleExpectation) => SingleExpectation) = 
    new OrderedExpectations(expectations.init :+ what(expectations.last))
}
