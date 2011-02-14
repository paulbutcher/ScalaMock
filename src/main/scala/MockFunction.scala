package com.borachio

trait MockFunction {
  def returns(r: Any)(implicit expectations: Expectations) {
    //! TODO
  }
}

class MockFunction0[T] extends Function0[T] with MockFunction {
  def apply() = null.asInstanceOf[T]
}
