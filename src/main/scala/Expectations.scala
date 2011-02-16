package com.borachio

abstract class Expectations(val expectations: List[Expectation]) extends Expectation {
  
  override def withArguments(arguments: Product) =
    applyToLast(_.withArguments(arguments))
    
  override def returning(value: Any) =
    applyToLast(_.returning(returnValue))

  override def times(n: Int) =
    applyToLast(_.times(n))
  
  override def then = newInstance(expectations :+ new SingleExpectation)

  protected def newInstance(expectations: List[Expectation]) : Expectations
  
  private def applyToLast(what: (Expectation) => Expectation) = 
    newInstance(expectations.init :+ what(expectations.last))
}
