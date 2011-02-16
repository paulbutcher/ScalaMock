package com.borachio

class OrderedExpectations(expectations: List[Expectation]) extends Expectation {
  
  override def withArguments(arguments: Product) =
    applyToLast(_.withArguments(arguments))
    
  override def returning(value: Any) =
    applyToLast(_.returning(value))

  override def times(n: Int) =
    applyToLast(_.times(n))
  
  override def then = new OrderedExpectations(expectations :+ new SingleExpectation)

  private def applyToLast(what: (Expectation) => Expectation) = 
    new OrderedExpectations(expectations.init :+ what(expectations.last))
}
