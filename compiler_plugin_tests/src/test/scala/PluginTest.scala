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

package com.borachio.plugin.test

import org.scalatest.FunSuite
import com.borachio.generated.GeneratedMockFactory
import com.borachio.scalatest.MockFactory
import com.borachio.{CallLogging, MockingURLClassLoader, VerboseErrors}
import java.net.URL

class PluginTest extends FunSuite with MockFactory with GeneratedMockFactory with VerboseErrors with CallLogging {
  
  def getClassLoader() = new MockingURLClassLoader(new URL("file:compiler_plugin_tests/target/scala_2.9.0/mock-classes/"))
  
  test("simple class") {
    val m = mock[SimpleClass]
    
    m.expects.nullMethod
    m.expects.methodWithOneArgument(42) returning "Expected return value"
    
    m.nullMethod
    expect("Expected return value") { m.methodWithOneArgument(42) }
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
    
    expect(90) { m.methodReturningFunction(10)() }
  }

  test("simple object") {
    val m = mockObject(SimpleObject)
    
    m.expects.sayHello returning "hola"
    
    expect("hola") { SimpleObject.sayHello }
  }

  //! TODO
  ignore("unmocked simple object") {
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
    m.expects.abstractMixinMethod(3) returning "mocked abstractMixinMethod"
    m.expects.concreteMixinMethod(4) returning "mocked concreteMixinMethod"
    m.expects.derivedClassMethod(5) returning "mocked derivedClassMethod"

    expect("mocked grandparentMethod") { m.grandparentMethod(1) }
    expect("mocked parentMethod") { m.parentMethod(2) }
    expect("mocked abstractMixinMethod") { m.abstractMixinMethod(3) }
    expect("mocked concreteMixinMethod") { m.concreteMixinMethod(4) }
    expect("mocked derivedClassMethod") { m.derivedClassMethod(5) }
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
  
  //! TODO
  ignore("case class") {
    val m = mock[CaseClass]
  }
}
