package com.borachio

trait MockFactory {
  
  def mockFunction[T] = new MockFunction0[T]
}
