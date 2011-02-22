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
    m1.forward(1.0)
    m2.forward(1.0)
    m1.penUp
    expect(2.0, 1.0) { m2.getPosition }

    verifyExpectations
  }
}
