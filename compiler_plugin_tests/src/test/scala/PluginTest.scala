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

import org.scalatest.Suite
import com.borachio.generated.GeneratedMockFactory
import com.borachio.scalatest.MockFactory
import com.borachio.{CallLogging, VerboseErrors}
import java.net.URL

class PluginTest extends Suite with MockFactory with GeneratedMockFactory with VerboseErrors with CallLogging {
  
  def mockClassPath = new URL("file:compiler_plugin_tests/target/scala_2.9.0/mock-classes/")
  
  def testSimpleClass {
    val m = mock[SimpleClass]
    
    m.expects.nullMethod
    m.expects.methodWithOneArgument(42) returning "Expected return value"
    
    m.nullMethod
    expect("Expected return value") { m.methodWithOneArgument(42) }
  }
  
  def testFinalClass {
    val m = mock[FinalClass]
    
    m.expects.nullMethod
    m.expects.methodWithOneArgument(42) returning "Expected return value"
    
    m.nullMethod
    expect("Expected return value") { m.methodWithOneArgument(42) }
  }
  
  def testClassWithFinalMethod {
    val m = mock[ClassWithFinalMethod]
    
    m.expects.nullMethod
    m.expects.methodWithOneArgument(42) returning "Expected return value"
    
    m.nullMethod
    expect("Expected return value") { m.methodWithOneArgument(42) }
  }
  
  def testSimpleTrait {
    val m = mock[SimpleTrait]
    
    m.expects.nullMethod
    m.expects.methodWithOneArgument(42) returning "Expected return value"
    
    m.nullMethod
    expect("Expected return value") { m.methodWithOneArgument(42) }
  }

  def testClassWithNonTrivialConstructor {
    val m = mock[ClassWithNonTrivialConstructor]
    
    m.expects.nullMethod
    m.expects.methodWithOneArgument(42) returning "Expected return value"
    
    m.nullMethod
    expect("Expected return value") { m.methodWithOneArgument(42) }
  }
  
  def testClassWithOverloadedMethods {
    val m = mock[ClassWithOverloadedMethods]
    
    //! Eugh - how do I avoid the necessity for this?
    m.expects.foo(new com.borachio.MockParameter$1(42)) returning "overload 1"
    m.expects.foo(1.23) returning "overload 2"
    m.expects.foo(42, 1.23) returning "overload 3"
    
    expect("overload 1") { m.foo(42) }
    expect("overload 2") { m.foo(1.23) }
    expect("overload 3") { m.foo(42, 1.23) }
  }

  def testClassWithPrivateConstructor {
    val m = mock[ClassWithPrivateConstructor]
    
    m.expects.nullMethod
    m.expects.methodWithOneArgument(42) returning "Expected return value"
    
    m.nullMethod
    expect("Expected return value") { m.methodWithOneArgument(42) }
  }
  
  def testOperator {
    val m = mock[SimpleClass]
    
    m.expects.+("foo") returning "foo added"
    
    expect("foo added") { m + "foo" }
  }
  
  def testWithoutMocks {
    expect("methodWithZeroArguments: (42,1.23)") { UsesClassWithNonTrivialConstructor.doSomething() }
  }
  
  def testSimpleObject {
    val m = mockObject(SimpleObject)
    
    m.expects.sayHello returning "hola"
    
    expect("hola") { SimpleObject.sayHello }
  }
  
  def testNonPrimitiveParameterType {
    val m = mock[SimpleClass]
    val x = new SimpleClass4
    
    m.expects.methodWithNonPrimitiveArgument(x)
    
    m.methodWithNonPrimitiveArgument(x)
  }

  //! TODO
  // def testUnmockedSimpleObject {
  //   expect("hello") { SimpleObject.sayHello }
  // }
}
