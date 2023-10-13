package com.paulbutcher.test

import org.scalamock.scalatest.MockFactory
import org.scalatest.funspec.AnyFunSpec

class VarSpec extends AnyFunSpec with MockFactory {

  autoVerify = false

  trait Vars {
    var aVar: String
    var concreteVar = "foo"
  }

  it("mock traits with vars") {
    withExpectations {
      val m = mock[Vars]
      (m.aVar_= _).expects("foo")
      (() => m.aVar).expects().returning("bar")
      m.aVar = "foo"
      assertResult("bar") {
        m.aVar
      }
    }
  }
}