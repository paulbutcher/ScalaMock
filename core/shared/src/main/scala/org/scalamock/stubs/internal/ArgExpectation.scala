package org.scalamock.stubs.internal

import org.scalamock.stubs.Stubs

trait ArgExpectation(using log: Stubs#CallLog):
  final def never(): Unit =
    setExpectation(0)
    
  final def once(): Unit =
    setExpectation(1)
    
  final def twice(): Unit =
    setExpectation(2)
    
  final def times(n: Int): Unit =
    setExpectation(n)

  protected def actualTimes: Int

  private def setExpectation(expected: Int): Unit = ???