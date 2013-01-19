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

package com.example.proxy.mockitostyle

import com.example.{Controller, Turtle}

import org.scalatest.FunSuite
import org.scalamock.scalatest.proxy.MockFactory
import scala.math.{Pi, sqrt}
 
class ControllerTest extends FunSuite with MockFactory {
 
  test("draw line") {
    val mockTurtle = stub[Turtle]
    val controller = new Controller(mockTurtle)

    inSequence {
      inAnyOrder {
        mockTurtle.when('getPosition)().returns(0.0, 0.0)
        mockTurtle.when('getAngle)().returns(0.0).once
      }
      mockTurtle.when('getAngle)().returns(Pi / 4)
    }
 
    controller.drawLine((1.0, 1.0), (2.0, 1.0))
    
    inSequence {
      mockTurtle.verify('turn)(~(Pi / 4))
      mockTurtle.verify('forward)(~sqrt(2.0))
      mockTurtle.verify('turn)(~(-Pi / 4))
      mockTurtle.verify('penDown)()
      mockTurtle.verify('forward)(1.0)
    }
  }
}