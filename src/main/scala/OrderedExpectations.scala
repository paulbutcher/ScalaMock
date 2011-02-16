package com.borachio

class OrderedExpectations(expectations: List[Expectation]) extends Expectations(expectations) {

  def newInstance(expectations: List[Expectation]) = new OrderedExpectations(expectations)
}
