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

import org.scalamock.scalatest.MockFactory
import org.scalatest.freespec.AnyFreeSpec

class HigherOrderFunctionsTest extends AnyFreeSpec with MockFactory {

  import language.postfixOps

  "HigherOrderFunctionsTest" - {

    "testMap" in {
      val f = mockFunction[Int, String]

      inSequence {
        f.expects(1).returning("one").once();
        f.expects(2).returning("two").once();
        f.expects(3).returning("three").once();
      }

      assertResult(Seq("one", "two", "three")) {
        Seq(1, 2, 3) map f
      }
    }

    "testRepeat" in {
      def repeat(n: Int)(what: => Unit): Unit = {
        for (i <- 0 until n)
          what
      }

      val f = mockFunction[Unit]
      f.expects().repeated(4).times()

      repeat(4) {
        f()
      }
    }

    "testFoldLeft" in {
      val f = mockFunction[String, Int, String]

      inSequence {
        f.expects("initial", 0).returning("intermediate one").once();
        f.expects("intermediate one", 1).returning("intermediate two"). once();
        f.expects("intermediate two", 2).returning("intermediate three").once();
        f.expects("intermediate three", 3).returning("final").once();
      }

      assertResult("final") {
        Seq(0, 1, 2, 3).foldLeft("initial")(f)
      }
    }
  }
}
