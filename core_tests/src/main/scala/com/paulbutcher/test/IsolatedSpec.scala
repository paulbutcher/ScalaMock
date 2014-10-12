package com.paulbutcher.test

import org.scalamock.scalatest.MockFactory
import org.scalatest.{ FlatSpec, ShouldMatchers, OneInstancePerTest }

class IsolatedSpec extends FlatSpec with MockFactory with ShouldMatchers with OneInstancePerTest {

  def repeat(n: Int)(what: => Unit) {
    for (i <- 0 until n)
      what
  }

}
