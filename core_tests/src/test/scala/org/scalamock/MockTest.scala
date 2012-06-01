// Copyright (c) 2011-2012 Paul Butcher
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

package org.scalamock

import org.scalamock.scalatest.MockFactory
import org.scalatest.FreeSpec

class MockTest extends FreeSpec with MockFactory {
  
  autoVerify = false
  
  trait TestTrait {
    def nullary: String
    def noParams(): String
    def oneParam(x: Int): String
    def twoParams(x: Int, y: Double): String
    
    def overloaded(x: Int): String
    def overloaded(x: String): String
    def overloaded(x: Int, y: Double): String
    
    def +(x: TestTrait): TestTrait
    
    def curried(x: Int)(y: Double): String
    def polymorphic[T](x: T): String
    def polycurried[T1, T2](x: T1)(y: T2): String
  }
  
  "Mocks should" - {
    "fail if an unexpected method call is made" in {
      val m = mock[TestTrait]
      intercept[ExpectationException] { m.oneParam(42) }
    }
    
    "allow expectations to be set" in {
      val m = mock[TestTrait]
      toMockFunction2(m.twoParams _).expects(42, 1.23).returning("a return value")
      expect("a return value") { m.twoParams(42, 1.23) }
      verifyExpectations
    }
    
    "cope with nullary methods" in {
      val m = mock[TestTrait]
      (m.nullary _).expects().returning("a return value")
      expect("a return value") { m.nullary }
      verifyExpectations
    }
    
    "cope with overloaded methods" in {
      val m = mock[TestTrait]
      (m.overloaded(_: Int)).expects(10).returning("got an integer")
      (m.overloaded(_: Int, _: Double)).expects(10, 1.23).returning("got two parameters")
      expect("got an integer") { m.overloaded(10) }
      expect("got two parameters") { m.overloaded(10, 1.23) }
      verifyExpectations
    }
    
    "cope with infix operators" in {
      val m1 = mock[TestTrait]
      val m2 = mock[TestTrait]
      val m3 = mock[TestTrait]
      (m1.+ _).expects(m2).returning(m3)
      expect(m3) { m1 + m2 }
      verifyExpectations
    }
    
    "cope with curried methods" in {
      val m = mock[TestTrait]
      (m.curried(_: Int)(_: Double)).expects(10, 1.23).returning("curried method called")
      val partial = m.curried(10) _
      expect("curried method called") { partial(1.23) }
      verifyExpectations
    }
    
    "cope with polymorphic methods" in {
      val m = mock[TestTrait]
      (m.polymorphic(_: Int)).expects(42).returning("called with integer")
      (m.polymorphic(_: String)).expects("foo").returning("called with string")
      expect("called with integer") { m.polymorphic(42) }
      expect("called with string") { m.polymorphic("foo") }
      verifyExpectations
    }
    
    "cope with curried polymorphic methods" in {
      val m = mock[TestTrait]
      toMockFunction2(m.polycurried(_: Int)(_: String)).expects(42, "foo").returning("it works")
      val partial = m.polycurried(42) _
      expect("it works") { partial("foo") }
      verifyExpectations
    }
  }
  
  "Stubs should" - {
    "return null unless told otherwise" in {
      val m = stub[TestTrait]
      expect(null) { m.oneParam(42) }
    }
  }
}