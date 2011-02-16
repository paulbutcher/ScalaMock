package com.borachio

abstract class Expectations(val expectations: List[Expectation]) extends Expectation {
  
  override def withArguments(arguments: Product) =
    applyToLast(_.withArguments(arguments))
    
  override def returning(returnValue: Any) =
    applyToLast(_.returning(returnValue))

  override def times(count: Int) =
    applyToLast(_.times(count))
  
  override def then = newInstance(expectations :+ new SingleExpectation)

  override def toString = expectations.mkString("[\n", "\n", "\n]")
  
  protected def newInstance(expectations: List[Expectation]) : Expectations
  
  private def applyToLast(what: (Expectation) => Expectation) = 
    newInstance(expectations.init :+ what(expectations.last))
}
