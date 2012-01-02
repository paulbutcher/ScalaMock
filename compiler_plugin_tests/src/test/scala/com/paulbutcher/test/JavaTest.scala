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
import org.scalamock.{CallLogging, VerboseErrors}

class JavaTest extends FunSuite with MockFactory with GeneratedMockFactory with VerboseErrors with CallLogging {
  
  val array = Array("foo", "bar", "baz")
  
  test("simple class") {
    val m = mock[SimpleJavaClass]
    
    m.expects.methodWithZeroArguments
    m.expects.methodWithOneArgument(42) returning "Expected return value"
    
    m.methodWithZeroArguments
    expect("Expected return value") { m.methodWithOneArgument(42) }
  }
  
  test("unmocked simple class") {
    val x = new SimpleJavaClass
    expect("methodWithOneArgument: 42") { x.methodWithOneArgument(42) }
  }
  
  test("array arguments") {
    val m = mock[SimpleJavaClass]

    m.expects.methodWithArrayArgument(42, array) returning "Expected return value"
    
    expect("Expected return value") { m.methodWithArrayArgument(42, array) }
  }
  
  test("unmocked array arguments") {
    val x = new SimpleJavaClass
    expect("methodWithArrayArgument: 42, [foo, bar, baz]") { x.methodWithArrayArgument(42, array) }
  }
  
  test("constants") {
    expect(1) { JavaClassWithConstants.INT_CONSTANT }
    expect(2) { JavaClassWithConstants.INTEGER_CONSTANT }
    expect(42) { JavaClassWithConstants.CLASS_CONSTANT.x }
    expect("foo") { JavaClassWithConstants.CLASS_CONSTANT.y }
    expect("Foo") { JavaClassWithConstants.STRING_CONSTANT }

    //! TODO - the following should also work
    // expect(new CaseClass(42, "foo")) { JavaClassWithConstants.CLASS_CONSTANT }
  }
  
  test("static variables") {
    expect(1.0F) { JavaClassWithStaticVars.floatVariable }
    expect(2.0) { JavaClassWithStaticVars.doubleVariable }

    JavaClassWithStaticVars.floatVariable = 1.23F
    JavaClassWithStaticVars.doubleVariable = 4.56

    expect(1.23F) { JavaClassWithStaticVars.floatVariable }
    expect(4.56) { JavaClassWithStaticVars.doubleVariable }
  }
}