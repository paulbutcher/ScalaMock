// Copyright (c) 2011 Paul Butcher
// 
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

package com.paulbutcher.test

import org.scalatest.FunSuite
import org.scalamock.generated.GeneratedMockFactory
import org.scalamock.scalatest.MockFactory
import org.scalamock.{CallLogging, ExpectationException, VerboseErrors, MockFunction0}

import scala.runtime.RichInt

class PluginTest extends FunSuite with MockFactory with GeneratedMockFactory with VerboseErrors with CallLogging {
  
  test("simple class") {
    val m = mock[SimpleClass]
    
    m.expects.nullMethod
    m.expects.methodWithOneArgument(42) returning "Expected return value"
    
    m.nullMethod
    expect("Expected return value") { m.methodWithOneArgument(42) }
  }
  
  test("predicate matching") {
    val m = mock[SimpleClass]
    
    m.expects.methodWithTwoArguments(where { _ < _ }) returning "less than" anyNumberOfTimes;
    m.expects.methodWithTwoArguments(where { _ > _ }) returning "greater than" anyNumberOfTimes
    
    expect("greater than") { m.methodWithTwoArguments(2, 1) }
    expect("less than") { m.methodWithTwoArguments(1, 2) }
  }
  
  test("computed return value") {
    val m = mock[SimpleClass]
    
    m.expects.methodWithTwoArguments(*, *) onCall { (x, y) => (x + y).toString } anyNumberOfTimes
    
    expect("3") { m.methodWithTwoArguments(1, 2) }
    expect("-1") { m.methodWithTwoArguments(0, -1) }
  }
  
  test("unmocked simple class") {
    val x = new SimpleClass
    expect("methodWithOneArgument: 42") { x.methodWithOneArgument(42) }
  }

  test("final class") {
    val m = mock[FinalClass]
    
    m.expects.nullMethod
    m.expects.methodWithOneArgument(42) returning "Expected return value"
    
    m.nullMethod
    expect("Expected return value") { m.methodWithOneArgument(42) }
  }
  
  test("unmocked final class") {
    val x = new FinalClass
    expect("methodWithOneArgument: 42") { x.methodWithOneArgument(42) }
  }
  
  test("class with final method") {
    val m = mock[ClassWithFinalMethod]
    
    m.expects.nullMethod
    m.expects.methodWithOneArgument(42) returning "Expected return value"
    
    m.nullMethod
    expect("Expected return value") { m.methodWithOneArgument(42) }
  }
  
  test("unmocked class with final method") {
    val x = new ClassWithFinalMethod
    expect("methodWithOneArgument: 42") { x.methodWithOneArgument(42) }
  }
  
  test("abstract class") {
    val m = mock[AbstractClass]
    
    m.expects.nullMethod
    m.expects.methodWithOneArgument(42) returning "Expected return value"
    
    m.nullMethod
    expect("Expected return value") { m.methodWithOneArgument(42) }
  }

  //! TODO
  ignore("unmocked abstract class") {
    val x = new AbstractClass {
      def methodWithOneArgument(x: Int) = "implemented: "+ x
    }
    expect("implemented: 42") { x.methodWithOneArgument(42) }
  }
  
  test("simple trait") {
    val m = mock[SimpleTrait]
    
    m.expects.nullMethod
    m.expects.methodWithOneArgument(42) returning "Expected return value"
    
    m.nullMethod
    expect("Expected return value") { m.methodWithOneArgument(42) }
  }
  
  test("unmocked simple trait") {
    val x = new SimpleTrait {
      def nullMethod = "implemented nullMethod"
      def methodWithZeroArguments() = "implemented methodWithZeroArguments"
      def methodWithOneArgument(x: Int) = "implemented: "+ x
      def methodWithTwoArguments(x: Int, y: Int) = "implemented: "+ (x, y)
    }
    expect("implemented: 42") { x.methodWithOneArgument(42) }
  }

  test("class with non-trivial constructor") {
    val m = mock[ClassWithNonTrivialConstructor]
    
    m.expects.nullMethod
    m.expects.methodWithOneArgument(42) returning "Expected return value"
    
    m.nullMethod
    expect("Expected return value") { m.methodWithOneArgument(42) }
  }
  
  test("unmocked class with non-trivial constructor") {
    val x = new ClassWithNonTrivialConstructor(42, 1.23)
    expect("methodWithOneArgument: 42") { x.methodWithOneArgument(42) }
  }
  
  test("unmocked class with non-trivial constructor indirectly") {
    expect("methodWithZeroArguments: (42,1.23)") { UsesClassWithNonTrivialConstructor.doSomething() }
  }
  
  test("class with overloaded methods") {
    val m = mock[ClassWithOverloadedMethods]

    // Using toMockParameter is necessary to avoid "ambiguous reference to overloaded definition"
    m.expects.foo(toMockParameter[Int](42)) returning "overload 1"
    m.expects.foo(1.23) returning "overload 2"
    m.expects.foo(42, 1.23) returning "overload 3"
    
    expect("overload 1") { m.foo(42) }
    expect("overload 2") { m.foo(1.23) }
    expect("overload 3") { m.foo(42, 1.23) }
  }

  test("class with private constructor") {
    val m = mock[ClassWithPrivateConstructor]
    
    m.expects.nullMethod
    m.expects.methodWithOneArgument(42) returning "Expected return value"
    
    m.nullMethod
    expect("Expected return value") { m.methodWithOneArgument(42) }
  }
  
  //! TODO
  ignore("unmocked class with private constructor") {
    val x = ClassWithPrivateConstructor.create
    expect("methodWithOneArgument: 42") { x.methodWithOneArgument(42) }
  }
  
  test("operator") {
    val m = mock[SimpleClass]
    
    m.expects.+("foo") returning "foo added"
    
    expect("foo added") { m + "foo" }
  }
  
  test("unmocked operator") {
    val x = new SimpleClass
    expect("SimpleClass foo") { x + " foo" }
  }

  test("non-primitive parameter type") {
    val m = mock[SimpleClass]
    val x = new SimpleClass4
    
    m.expects.methodWithNonPrimitiveArgument(x)
    
    m.methodWithNonPrimitiveArgument(x)
  }
  
  test("method returning a function") {
    val m = mock[SimpleClass]
    val f = mockFunction[Int, Int]
    
    m.expects.methodReturningFunction(10) returning f
    f.expects(9) returning 90
    
    expect(90) { m.methodReturningFunction(10)(9) }
  }
  
  test("curried method") {
    val m = mock[SimpleClass]
    
    m.expects.curriedMethod(42, 1.23)("foo", false) returning "curried method called"
    
    val partiallyApplied = m.curriedMethod(42, 1.23) _
    expect("curried method called") { partiallyApplied("foo", false) }
  }
  
  test("unmocked curried method") {
    expect("curriedMethod: (42,1.23,foo,true)") { (new SimpleClass).curriedMethod(42, 1.23)("foo", true) }
  }
  
  test("implicit parameters") {
    implicit val i1 = new ImplicitTest1
    implicit val i2 = new ImplicitTest2

    val m = mock[SimpleClass]
    
    inSequence {
      m.expects.withImplicitParameters(1)(i1, i2) returning "implicits supplied"
      m.expects.withImplicitParameters(1) returning "implicits not supplied"
    }
    
    expect("implicits supplied") { m.withImplicitParameters(1) }
    expect("implicits not supplied") { m.withImplicitParameters(1)(new ImplicitTest1, new ImplicitTest2) }
  }
  
  test("with view bound") {
    val m = mock[SimpleClass]
    
    m.expects.withViewBound(3, 4) returning true
    
    expect(true) { m.withViewBound(3, 4) }
  }
  
  test("polymorphic method") {
    val m = mock[SimpleClass]
    
    m.expects.polymorphicMethod(42, "foo", 1.23) returning (1, "bar", 3.14)
    m.expects.polymorphicMethod(1, true, List("foo", "bar")) returning (0, false, List("alice"))
    
    expect((0, false, List("alice"))) { m.polymorphicMethod(1, true, List("foo", "bar")) }
    expect((1, "bar", 3.14)) { m.polymorphicMethod(42, "foo", 1.23) }
  }
  
  test("upper bound") {
    val m = mock[SimpleClass]
    
    //! TODO - Find a way to make the explicit type unnecessary
    m.expects.withUpperBound(where { (x: Product) => x.productArity == 2 })
    
    m.withUpperBound((1, 2))
  }
  
  test("repeated parameter") {
    val m = mock[SimpleClass]
    
    //! TODO - Find a way to allow expectations to allow expects to take
    //! repeated parameters instead of a Seq
    m.expects.repeatedParameter(42, Seq("foo", "bar", "baz")) returning "Expected return value"
    
    expect("Expected return value") { m.repeatedParameter(42, "foo", "bar", "baz") }
  }

  test("simple object") {
    val m = mockObject(SimpleObject)
    
    m.expects.sayHello returning "hola"
    
    expect("hola") { SimpleObject.sayHello }
  }

  test("unmocked simple object") {
    expect("hello") { SimpleObject.sayHello }
  }

  test("class with companion object") {
    val m1 = mock[ClassWithCompanionObject]
    val m2 = mockObject(ClassWithCompanionObject)
    
    m1.expects.normalMethod2(42) returning "normal method called"
    m2.expects.companionObjectMethod2(2001) returning "a space oddessy"
    
    expect("normal method called") { m1.normalMethod2(42) }
    expect("a space oddessy") { ClassWithCompanionObject.companionObjectMethod2(2001) }
  }

  test("trait with companion object") {
    val m1 = mock[TraitWithCompanionObject]
    val m2 = mockObject(TraitWithCompanionObject)
    
    m1.expects.normalMethod2(42) returning "normal method called"
    m2.expects.companionObjectMethod2(2001) returning "a space oddessy"
    
    expect("normal method called") { m1.normalMethod2(42) }
    expect("a space oddessy") { TraitWithCompanionObject.companionObjectMethod2(2001) }
  }
  
  test("derived class") {
    val m = mock[DerivedClass]
    
    m.expects.grandparentMethod(1) returning "mocked grandparentMethod"
    m.expects.parentMethod(2) returning "mocked parentMethod"
    m.expects.abstractMixin1Method(3) returning "mocked abstractMixin1Method"
    m.expects.concreteMixin1Method(4) returning "mocked concreteMixin1Method"
    m.expects.derivedClassMethod(5) returning "mocked derivedClassMethod"
    m.expects.mixin2Method(6) returning "mocked mixin2Method"

    expect("mocked grandparentMethod") { m.grandparentMethod(1) }
    expect("mocked parentMethod") { m.parentMethod(2) }
    expect("mocked abstractMixin1Method") { m.abstractMixin1Method(3) }
    expect("mocked concreteMixin1Method") { m.concreteMixin1Method(4) }
    expect("mocked derivedClassMethod") { m.derivedClassMethod(5) }
    expect("mocked mixin2Method") { m.mixin2Method(6) }
  }
  
  test("val") {
    val m = mock[ClassWithValsAndVars]
    
    m.expects.aVal returning 2001
    
    expect(2001) { m.aVal }
  }
  
  test("var") {
    val m = mock[ClassWithValsAndVars]
    
    m.expects.aVar = "a new value"
    m.expects.aVar returning "another value"
    
    m.aVar = "a new value"
    expect("another value") { m.aVar }
  }
  
  test("class with nested types") {
    val m1 = mock[ClassWithNestedTypes]
    val m2 = m1.mock[ClassWithNestedTypes#NestedClass]
    val m3 = m2.mock[ClassWithNestedTypes#NestedClass#DoublyNestedClass]
    
    m1.expects.nonNestedMethod returning m2
    m2.expects.nestedClassMethod returning m3
    m3.expects.doublyNestedMethod returning "I'm deeply nested"
    
    expect("I'm deeply nested") { m1.nonNestedMethod.nestedClassMethod.doublyNestedMethod }
  }
  
  //! TODO
  ignore("unmocked class with nested types") {
    val x = new ClassWithNestedTypes
    
    expect("doubly nested method") { x.nonNestedMethod.nestedClassMethod.doublyNestedMethod }
  }
  
  test("parameterised class") {
    val m = mock[ParameterisedClass[Double, List[String]]]
    
    m.expects.normalMethod(42, "foo") returning "normalMethod called"
    m.expects.referencesParameters(1.23, List("foo", "bar")) returning "referencesParameters called"
    m.expects.polymorphic(true, 3.14) returning "polymorphic called"
    
    expect("normalMethod called") { m.normalMethod(42, "foo") }
    expect("referencesParameters called") { m.referencesParameters(1.23, List("foo", "bar")) }
    expect("polymorphic called") { m.polymorphic(true, 3.14) }
  }
  
  test("unmocked parameterised class") {
    val x = new ParameterisedClass[Double, List[String]]

    expect("normalMethod: (42,foo)") { x.normalMethod(42, "foo") }
    expect("referencesParameters: (1.23,List(foo, bar))") { x.referencesParameters(1.23, List("foo", "bar")) }
    expect("polymorphic: (true,3.14)") { x.polymorphic(true, 3.14) }
  }
}
