package com.borachio

abstract class MockFunction(expectations: Expectations) {

  protected def handle(arguments: Product) = expectations.handle(this, arguments)
}

class MockFunction0[R](expectations: Expectations) extends MockFunction(expectations) with Function0[R] {
  def apply() = handle(None).asInstanceOf[R]
}

class MockFunction1[T1, R](expectations: Expectations) extends MockFunction(expectations) with Function1[T1, R] {
  def apply(v1: T1) = handle(new Tuple1(v1)).asInstanceOf[R]
}

class MockFunction2[T1, T2, R](expectations: Expectations) extends MockFunction(expectations) with Function2[T1, T2, R] {
  def apply(v1: T1, v2: T2) = handle((v1, v2)).asInstanceOf[R]
}
