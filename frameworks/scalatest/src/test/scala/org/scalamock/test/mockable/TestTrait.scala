package org.scalamock.test.mockable

trait TestTrait {
  def noParamMethod(): String
  def oneParamMethod(param: Int): String
  def polymorphicMethod[T](x: List[T]): String
}