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

class ProxyMockTest extends Suite with MockFactory with ProxyMockFactory {
  
  autoVerify = false
  
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
}
