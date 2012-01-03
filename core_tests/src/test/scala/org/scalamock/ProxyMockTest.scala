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

package org.scalamock

import org.scalamock.scalatest.MockFactory
import org.scalatest.Suite

class ProxyMockTest extends Suite with MockFactory with ProxyMockFactory with VerboseErrors {
  
  autoVerify = false
  
  trait Turtle {
    def penUp()
    def penDown()
    def forward(distance: Double): (Double, Double)
    def turn(angle: Double)
    def getAngle: Double
    def getPosition(): (Double, Double)
    def setPosition(x: Double, y: Double): (Double, Double)
    def moveInSequence(positions: (Double, Double)*)
  }
  
  def testUnexpectedCall {
    val m = mock[Turtle]
    intercept[ExpectationException] { m.penDown }
  }

  def testSingleExpectation {
    val m = mock[Turtle]
    m expects 'setPosition withArguments (1.0, 2.0) returning (3.0, 4.0)
    expect((3.0, 4.0)) { m.setPosition(1.0, 2.0) }
    verifyExpectations
  }
  
  def testMultipleExpectationsOnMultipleObjects {
    val m1 = mock[Turtle]
    val m2 = mock[Turtle]
    
    inSequence {
      m1 expects 'setPosition withArguments (0.0, 0.0)
      m1 expects 'penDown
      m1 expects 'forward withArguments (10.0)
      m1 expects 'penUp
    }
    inSequence {
      m2 expects 'setPosition withArguments(1.0, 1.0)
      m2 expects 'turn withArguments (90.0)
      m2 expects 'forward withArguments (1.0)
      m2 expects 'getPosition returning (2.0, 1.0)
    }
    
    m2.setPosition(1.0, 1.0)
    m1.setPosition(0.0, 0.0)
    m1.penDown
    m2.turn(90.0)
    m1.forward(10.0)
    m2.forward(1.0)
    m1.penUp
    expect((2.0, 1.0)) { m2.getPosition }

    verifyExpectations
  }
  
  def testRepeatedParameters {
    val m = mock[Turtle]
    
    m expects 'moveInSequence withArguments(**((1.0, 1.0)))
    m expects 'moveInSequence withArguments(**((2.0, 2.0), (-1.0, -2.0), *)) anyNumberOfTimes
    
    m.moveInSequence((1.0, 1.0))
    m.moveInSequence((2.0, 2.0), (-1.0, -2.0), (10.0, 0.0))
    m.moveInSequence((2.0, 2.0), (-1.0, -2.0), (1.0, 2.0))
    
    verifyExpectations
  }
  
  def testStubs {
    val m = mock[Turtle]
    
    m stubs 'setPosition
    m stubs 'getPosition returning (3.0, 4.0)
    m stubs 'forward

    m.setPosition(1.0, 2.0)
    expect((3.0, 4.0)) { m.getPosition }
    m.setPosition(5.0, 6.0)
    
    verifyExpectations
  }
  
  def testPredicate {
    val m = mock[Turtle]
    
    m expects 'setPosition where { (x: Double, y: Double) => x == y } anyNumberOfTimes
    
    m.setPosition(1.0, 1.0)
    m.setPosition(3.14159, 3.14159)
    intercept[ExpectationException] { m.setPosition(1.0, 2.0) }
  }
  
  def testMockReturningMock {
    trait Parent {
      def getChild: Child
    }
    trait Child
    
    val p = mock[Parent]
    val c = mock[Child]
    
    p expects 'getChild returning c
    
    expect(c) { p.getChild }
    
    verifyExpectations
  }
}
