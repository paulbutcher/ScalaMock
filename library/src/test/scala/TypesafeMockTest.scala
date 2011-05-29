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
    
    def expects(name: Symbol) = mocks(name).toExpectation
    
    def penUp() { mocks('penUp)(Array[AnyRef]()) }
    def penDown() { mocks('penDown)(Array[AnyRef]()) }
    def forward(distance: Double) = mocks('forward)(Array[AnyRef](distance.asInstanceOf[AnyRef])).asInstanceOf[Double]
    def turn(angle: Double) { mocks('turn)(Array[AnyRef](angle.asInstanceOf[AnyRef])) }
    def getAngle: Double = mocks('getAngle)(Array[AnyRef]()).asInstanceOf[Double]
    def getPosition(): (Double, Double) = mocks('getPosition)(Array[AnyRef]()).asInstanceOf[(Double, Double)]
    def setPosition(x: Double, y: Double): (Double, Double) = mocks('setPosition)(Array[AnyRef](x.asInstanceOf[AnyRef], y.asInstanceOf[AnyRef])).asInstanceOf[(Double, Double)]
  
    private val mocks = Map[Symbol, ProxyMockFunction](
        'penUp -> new ProxyMockFunction('penUp, TypesafeMockTest.this),
        'penDown -> new ProxyMockFunction('penDown, TypesafeMockTest.this),
        'forward -> new ProxyMockFunction('forward, TypesafeMockTest.this),
        'turn -> new ProxyMockFunction('turn, TypesafeMockTest.this),
        'getAngle -> new ProxyMockFunction('getAngle, TypesafeMockTest.this),
        'getPosition -> new ProxyMockFunction('getPosition, TypesafeMockTest.this),
        'setPosition -> new ProxyMockFunction('setPosition, TypesafeMockTest.this)
      )
  }
  
  def testUnexpectedCall {
    val m = new MockTurtle
    intercept[ExpectationException] { m.penDown }
  }

  def testSingleExpectation {
    val m = new MockTurtle
    m expects 'setPosition withArguments (1.0, 2.0) returning (3.0, 4.0)
    expect((3.0, 4.0)) { m.setPosition(1.0, 2.0) }
    verifyExpectations
  }
}