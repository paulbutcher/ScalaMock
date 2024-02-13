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

package com.example.mockitostyle

import org.scalamock.scalatest.MockFactory
import org.scalatest.freespec.AnyFreeSpec

class HigherOrderFunctionsTest extends AnyFreeSpec with MockFactory {
  import language.postfixOps

  "HigherOrderFunctionsTest" - {

    "testMap" in {
      val f = stubFunction[Int, String]

      f.when(1).returns("one")
      f.when(2).returns("two")
      f.when(3).returns("three")

      assertResult(Seq("one", "two", "three")) {
        Seq(1, 2, 3) map f
      }

      inSequence {
        f.verify(1).once();
        f.verify(2).once();
        f.verify(3).once();
      }
    }

    "testRepeat" in {
      def repeat(n: Int)(what: => Unit): Unit = {
        for (i <- 0 until n)
          what
      }

      val f = stubFunction[Unit]

      repeat(4) {
        f()
      }

      f.verify().repeated(4).times()
    }

    "testFoldLeft" in {
      val f = stubFunction[String, Int, String]

      f when("initial", 0) returns "intermediate one"
      f when("intermediate one", 1) returns "intermediate two"
      f when("intermediate two", 2) returns "intermediate three"
      f when("intermediate three", 3) returns "final"

      assertResult("final") {
        Seq(0, 1, 2, 3).foldLeft("initial")(f)
      }

      inSequence {
        f.verify("initial", 0).once();
        f.verify("intermediate one", 1).once();
        f.verify("intermediate two", 2).once();
        f.verify("intermediate three", 3).once();
      }
    }
  }
}
