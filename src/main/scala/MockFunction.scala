package com.borachio

abstract class MockFunction(expectations: Expectations) {

  protected def handle() = expectations.handle(this)
}

class MockFunction0[T](expectations: Expectations) extends MockFunction(expectations) with Function0[T] {
  def apply() = handle().asInstanceOf[T]
}
