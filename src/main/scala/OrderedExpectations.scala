package com.borachio

class OrderedExpectations(expectations: List[SingleExpectation]) extends Expectation {
  
  override def withArguments(arguments: Product) =
    applyToLast(_.withArguments(arguments))
    
  override def returning(value: Any) =
    applyToLast(_.returning(value))

  override def times(n: Int) =
    applyToLast(_.times(n))
  
  override def then = new OrderedExpectations(expectations :+ new SingleExpectation(expectations.last.target))
  
  override def handle(arguments: Product) = {
    if(currentExpectation == null || currentExpectation.exhausted) {
      if (!expectationIterator.hasNext)
        throw new ExpectationException("Unexpected call")
      currentExpectation = expectationIterator.next
    }
    currentExpectation.handle(arguments)
  }
  
  override def satisfied = false //! TODO
  
  override def exhausted = false //! TODO

  private def applyToLast(what: (SingleExpectation) => SingleExpectation) = 
    new OrderedExpectations(expectations.init :+ what(expectations.last))

  private val expectationIterator = expectations.iterator
  private var currentExpectation: Expectation = _
}
