package com.paulbutcher.test.mock

import com.paulbutcher.test._

class JavaMocksTest extends IsolatedSpec {
  val javaMock = mock[JavaClassWithOverloadedMethod]

  behavior of "ScalaMock while mocking Java classes and interfaces"

  it should "mock classes with bridged methods" in {
    val m = mock[JavaClassWithBridgeMethod]

    (m.compare _).expects(new Integer(5)).returning(1)
    (m.compare _).expects(new Integer(6)).returning(2)

    def useBridgeMethod[T](gen: JavaGenericInterface[T], x: T) = {
      gen.compare(x)
    }

    assertResult(1) { m.compare(new Integer(5)) } // calls: int compare(Integer)
    assertResult(2) { useBridgeMethod(m, new Integer(6)) } // calls: int compare(Object)
  }

  //! TODO - this is going to have to wait for macro types for a proper solution
  //    "cope with Java methods with repeated parameters" in {
  //      withExpectations {
  //        val m = mock[JavaInterface]
  //        (m.repeatedParam _).expects(42, Seq(1.23, 4.56))
  //        m.repeatedParam(42, 1.23, 4.56)
  //      }
  //    }

  it should "mock a Java interface" in {
    val m = mock[JavaInterface]
    (m.m _).expects(42, "foo").returning("a return value")
    assertResult("a return value") { m.m(42, "foo") }
  }

  it should "mock a Polymorhpic Java interface" in { // test for issue #24
    val m = mock[PolymorphicJavaInterface]
    (m.simplePolymorphicMethod _).expects("foo").returning(44)
    assertResult(44) { m.simplePolymorphicMethod("foo") }
  }

  it should "mock a Java class with an overloaded method" in { // test for issue #34
    val m = mock[JavaClassWithOverloadedMethod]
    (m.overloadedMethod(_: String)).expects("a").returning("first")
    (m.overloadedMethod(_: String, _: String)).expects("a", "b").returning("second")
    assertResult("first") { m.overloadedMethod("a") }
    assertResult("second") { m.overloadedMethod("a", "b") }
  }
}
