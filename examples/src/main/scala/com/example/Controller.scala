// Copyright (c) 2011-2015 ScalaMock Contributors (https://github.com/paulbutcher/ScalaMock/graphs/contributors)
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

package com.example

import scala.math.{atan2, sqrt}
 
class Controller(turtle: Turtle) {
 
  def drawLine(start: (Double, Double), end: (Double, Double)): Unit = {
    moveTo(start)
 
    val initialAngle = turtle.getAngle
    val deltaPos = delta(start, end)
 
    turtle.turn(angle(deltaPos) - initialAngle)
    turtle.penDown()
    turtle.forward(distance(deltaPos))
  }
 
  def delta(pos1: (Double, Double), pos2: (Double, Double)) = 
    (pos2._1 - pos1._1, pos2._2 - pos1._2)
 
  def distance(delta: (Double, Double)) = 
    sqrt(delta._1 * delta._1 + delta._2 * delta._2)
 
  def angle(delta: (Double, Double)) = 
    atan2(delta._2, delta._1)
 
  def moveTo(pos: (Double, Double)): Unit = {
    val initialPos = turtle.getPosition
    val initialAngle = turtle.getAngle
 
    val deltaPos = delta(initialPos, pos)
 
    turtle.penUp()
    turtle.turn(angle(deltaPos) - initialAngle)
    turtle.forward(distance(deltaPos))
  }
}
