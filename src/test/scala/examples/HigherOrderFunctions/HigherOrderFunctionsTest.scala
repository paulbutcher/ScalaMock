package com.borachio.examples.mocksarentstubs

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
      f expects ("zero", 1) returning "one" once;
      f expects ("one", 2) returning "two" once;
      f expects ("two", 3) returning "three" once;
      f expects ("three", 4) returning "four" once;
    }

    expect("four") { Seq(1, 2, 3, 4).foldLeft("zero")(f) }
  }
}
