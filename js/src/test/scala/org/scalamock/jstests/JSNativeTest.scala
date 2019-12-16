package org.scalamock.jstests

import org.scalamock.scalatest.MockFactory

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

@js.native
@JSGlobal
class FakeJSNativeClass extends js.Object {
  def fillText(text: String, x: Double, y: Double, maxWidth: Double = js.native): Unit = js.native
}

class JSNativeTest extends AnyFlatSpec with MockFactory with Matchers {
  ignore should "create a mock for method with 'js.native' default args" in {
//    val _ = mock[FakeJSNativeClass]
  }
}
