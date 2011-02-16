package com.borachio

trait MockFunction {
}

class MockFunction0[T] extends Function0[T] with MockFunction {
  def apply() = null.asInstanceOf[T]
}
