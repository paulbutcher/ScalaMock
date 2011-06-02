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

package com.borachio

import com.borachio.scalatest.MockFactory
import org.scalatest.Suite
import scala.collection.mutable.Map

class TypesafeMockTest extends Suite with MockFactory {
  
  autoVerify = false

  class MockTurtle extends Turtle {
    
    val expects = new {
      def penUp() = mock$penUp.expects
      def penDown() = mock$penDown.expects
      def forward(distance:Double) = mock$forward.expects(distance)
      def turn(angle: Double) = mock$turn(angle)
      def getAngle = mock$getAngle
      def getPosition = mock$getPosition
      def setPosition(x: Double, y: Double) = mock$setPosition.expects(x, y)
    }
    
    def penUp() = mock$penUp()
    def penDown() = mock$penDown()
    def forward(distance: Double) = mock$forward(distance)
    def turn(angle: Double) = mock$turn(angle)
    def getAngle = mock$getAngle()
    def getPosition = mock$getPosition()
    def setPosition(x: Double, y: Double) = mock$setPosition(x, y)

    private val mock$penUp = new MockFunction0[Unit](TypesafeMockTest.this, 'penUp)
    private val mock$penDown = new MockFunction0[Unit](TypesafeMockTest.this, 'penDown)
    private val mock$forward = new MockFunction1[Double, Unit](TypesafeMockTest.this, 'forward)
    private val mock$turn = new MockFunction1[Double, Unit](TypesafeMockTest.this, 'turn)
    private val mock$getAngle = new MockFunction0[Double](TypesafeMockTest.this, 'getAngle)
    private val mock$getPosition = new MockFunction0[(Double, Double)](TypesafeMockTest.this, 'getPosition)
    private val mock$setPosition = new MockFunction2[Double, Double, (Double, Double)](TypesafeMockTest.this, 'setPosition)
  }
  
  def testUnexpectedCall {
    val m = new MockTurtle
    intercept[ExpectationException] { m.penDown }
  }

  def testSingleExpectation {
    val m = new MockTurtle
    m.expects.setPosition(1.0, 2.0) returning(3.0, 4.0)
    expect((3.0, 4.0)) { m.setPosition(1.0, 2.0) }
    verifyExpectations
  }
}