package com.borachio

class Mock2[T1, T2, R](context: MockContext) extends Function2[T1, T2, R] {

  def apply(v1: T1, v2: T2) = context.handle[T1, T2, R](this, v1, v2)
}
