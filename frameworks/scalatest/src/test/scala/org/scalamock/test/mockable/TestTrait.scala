package org.scalamock.test.mockable

trait TestTrait {
  def noParamMethod(): String
  def oneParamMethod(param: Int): String
}