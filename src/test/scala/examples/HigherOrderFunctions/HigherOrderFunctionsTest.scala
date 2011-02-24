package com.borachio.examples

import org.scalatest.Suite
import com.borachio.MockFactory

class HigherOrderFunctionsTest extends Suite with MockFactory {
  
  def testMap() {
    val f = mockFunction[Int, String]
    
    inSequence {
      f expects (1) returning "one" once;
      f expects (2) returning "two" once;
      f expects (3) returning "three" once;
    }
    
    expect(Seq("one", "two", "three")) { Seq(1, 2, 3) map f }
  }
  
  def testRepeat() {
    def repeat(n: Int)(what: => Unit) {
      for (i <- 0 until n)
        what
    }
    
    val f = mockFunction[Unit]
    f expects () repeat 4
    
    repeat(4) { f() }
  }
  
  def testFoldLeft() {
    val f = mockFunction[String, Int, String]
    
    inSequence {
      f expects ("initial", 0) returning "intermediate one" once;
      f expects ("intermediate one", 1) returning "intermediate two" once;
      f expects ("intermediate two", 2) returning "intermediate three" once;
      f expects ("intermediate three", 3) returning "final" once;
    }

    expect("final") { Seq(0, 1, 2, 3).foldLeft("initial")(f) }
  }
}
