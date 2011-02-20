package com.borachio

abstract class MockFunction(expectations: Expectations) {

  protected def handle(arguments: Product) = expectations.handle(this, arguments)
}

class MockFunction0[T](expectations: Expectations) extends MockFunction(expectations) with Function0[T] {
  def apply() = handle(None).asInstanceOf[T]
}
