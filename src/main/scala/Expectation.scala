package com.borachio

trait Expectation

class Expectation2[T1, T2, R] extends Expectation {
  
  var expectedArguments: Option[Product2[T1, T2]] = None
  var returnValue: R = _
}
