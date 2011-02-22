package com.borachio

import org.scalatest.Suite

class ProxyMockTest extends Suite with MockFactory {
  
  autoVerify = false
  
  trait Turtle {
    def penUp()
    def penDown()
    def forward(distance: Double): (Double, Double)
    def turn(angle: Double): Double
    def getAngle: Double
    def getPosition(): (Double, Double)
    def setPosition(x: Double, y: Double): (Double, Double)
  }

  def testSingleExpectation {
    val m = mock[Turtle]
    m expects 'setPosition withArguments (1.0, 2.0) returning (3.0, 4.0)
    expect((3.0, 4.0)) { m.setPosition(1.0, 2.0) }
  }
}
