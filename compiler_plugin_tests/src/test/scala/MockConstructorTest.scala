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
import com.borachio.{CallLogging, ExpectationException, MockingURLClassLoader, VerboseErrors}
import java.net.URL

class MockConstructorTest extends Suite with MockFactory with GeneratedMockFactory with VerboseErrors with CallLogging {
  
  def getClassLoader() = new MockingURLClassLoader(new URL("file:compiler_plugin_tests/target/scala-2.9.0.final/mock-classes/"))
  
  def testExpectNewInstanceSimple {
    val m = mock[SimpleClass]
    
    m.expects.newInstance.once
  
    new SimpleClass
  }
  
  def testExpectNewInstanceWithArgs {
    val m = mock[ClassWithNonTrivialConstructor]
    
    m.expects.newInstance(42, 1.23).once
    m.expects.methodWithZeroArguments() returning "some different return value"
    
    expect("some different return value") { UsesClassWithNonTrivialConstructor.doSomething() }
  }
  
  def testThrowingConstructor {
    val m = mock[ClassWithNonTrivialConstructor]
  
    m.expects.newInstance(42, 1.23) throws new RuntimeException("oops")
  
    intercept[RuntimeException] { UsesClassWithNonTrivialConstructor.doSomething() }
  }
  
  def testMultipleIdenticalInstances {
    val m = mock[ClassWithNonTrivialConstructor]
    
    m.expects.newInstance(42, 1.23).twice
    
    new ClassWithNonTrivialConstructor(42, 1.23)
    new ClassWithNonTrivialConstructor(42, 1.23)
  }
  
  def testMultipleInstances {
    val m1 = mock[ClassWithNonTrivialConstructor]
    val m2 = mock[ClassWithNonTrivialConstructor]
    val m3 = mock[SimpleClass]
    val m4 = mock[FinalClass]
    
    m1.expects.newInstance(1, 2.0)
    m1.expects.methodWithZeroArguments() returning "m1"
    m2.expects.newInstance(3, 4.0)
    m2.expects.methodWithTwoArguments(10, 20) returning "m2"
    m3.expects.newInstance
    m3.expects.nullMethod
    m4.expects.newInstance
    m4.expects.methodWithOneArgument(42) returning "m4"
    
    val x2 = new ClassWithNonTrivialConstructor(3, 4.0)
    val x3 = new SimpleClass
    val x1 = new ClassWithNonTrivialConstructor(1, 2.0)
    val x4 = new FinalClass
    
    expect("m4") { x4.methodWithOneArgument(42) }
    expect("m1") { x1.methodWithZeroArguments() }
    m3.nullMethod
    expect("m2") { x2.methodWithTwoArguments(10, 20) }
  }

  def testNestedMocks {
    val m = mock[SimpleClass2]
    
    m.expects.newInstance
    m.expects.doSomething(42) returning "did something"
    
    val x = new SimpleClass
    expect("did something") { x.callSimpleClass2(42) }
  }
  
  def testDeeplyNestedMocks {
    val m = mock[SimpleClass4]
    
    m.expects.newInstance
    m.expects.doSomething(42) returning "deep, man"
    
    val x = new SimpleClass
    expect("deep, man") { x.nestDeeply(42) }
  }
}
