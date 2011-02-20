package com.borachio

class MockFunction0[T] extends Function0[T] {
  def apply() = null.asInstanceOf[T]
}
