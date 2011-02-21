package com.borachio

private[borachio] trait Handler {
  
  def handle(mock: MockFunction, arguments: Product): Option[Any]
  def satisfied: Boolean
}
