package com.borachio

trait Expectation {
  def withArguments(arguments: Product): Expectation
  def returning(returnValue: Any): Expectation
  def times(count: Int): Expectation
  def then: Expectation

  def never = times(0)
  def once = times(1)
  def twice = times(2)
}
