package com.borachio

import org.scalatest.{BeforeAndAfterEach, Suite}

trait MockFactory extends BeforeAndAfterEach { this: Suite =>
  
  def mockFunction0[T] = new MockFunction0[T]

  override def beforeEach() {
    // expectations = new UnorderedExpectations
  }
  
  override def afterEach() {
    //! TODO
    // if (autoVerify)
    //   verifyExpectations
  }
  
  protected def disableAutoVerify() {
    autoVerify = false
  }
  
  protected def verifyExpectations() {
    //! TODO
    // expectations.verify
  }

  implicit protected var expectations: Expectations = _
  
  private var autoVerify = true
}
