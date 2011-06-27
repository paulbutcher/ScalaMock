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
import java.net.URL

class PluginTest extends Suite with MockFactory with GeneratedMockFactory {
  
  def mockClassPath = new URL("file:compiler_plugin_tests/target/scala_2.9.0/mock-classes/")
  
  def testSimpleClass {
    val mockSimpleClass = mock[SimpleClass]
    
    mockSimpleClass.expects.nullMethod
    mockSimpleClass.expects.methodWithOneArgument(42) returning "Expected return value"
    
    mockSimpleClass.nullMethod
    expect("Expected return value") { mockSimpleClass.methodWithOneArgument(42) }
  }
  
  def testFinalClass {
    val mockFinalClass = mock[FinalClass]
    
    mockFinalClass.expects.nullMethod
    mockFinalClass.expects.methodWithOneArgument(42) returning "Expected return value"
    
    mockFinalClass.nullMethod
    expect("Expected return value") { mockFinalClass.methodWithOneArgument(42) }
  }
  
  def testClassWithFinalMethod {
    val mockClassWithFinalMethod = mock[ClassWithFinalMethod]
    
    mockClassWithFinalMethod.expects.nullMethod
    mockClassWithFinalMethod.expects.methodWithOneArgument(42) returning "Expected return value"
    
    mockClassWithFinalMethod.nullMethod
    expect("Expected return value") { mockClassWithFinalMethod.methodWithOneArgument(42) }
  }
  
  def testSimpleTrait {
    val mockSimpleTrait = mock[SimpleTrait]
    
    mockSimpleTrait.expects.nullMethod
    mockSimpleTrait.expects.methodWithOneArgument(42) returning "Expected return value"
    
    mockSimpleTrait.nullMethod
    expect("Expected return value") { mockSimpleTrait.methodWithOneArgument(42) }
  }

  def testClassWithNonTrivialConstructor {
    val mockClassWithNonTrivialConstructor = mock[ClassWithNonTrivialConstructor]
    
    mockClassWithNonTrivialConstructor.expects.nullMethod
    mockClassWithNonTrivialConstructor.expects.methodWithOneArgument(42) returning "Expected return value"
    
    mockClassWithNonTrivialConstructor.nullMethod
    expect("Expected return value") { mockClassWithNonTrivialConstructor.methodWithOneArgument(42) }
  }
}
