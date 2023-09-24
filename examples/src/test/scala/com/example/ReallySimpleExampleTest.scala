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

import com.example.Greetings.Formatter
import org.scalamock.scalatest.MockFactory
import org.scalatest.funsuite.AnyFunSuite

class ReallySimpleExampleTest extends AnyFunSuite with MockFactory {
  test("Hello") {
    val mockFormatter = mock[Formatter]

    (mockFormatter.format _).expects("Mr Bond").returning("Ah, Mr Bond. I've been expecting you").once()

    Greetings.sayHello("Mr Bond", mockFormatter)
  }

  test("MockitoStyle") {
    val mockFormatter = stub[Formatter]
    val bond = "Mr Bond"

    (mockFormatter.format _).when(bond).returns("Ah, Mr Bond. I've been expecting you")

    Greetings.sayHello(bond, mockFormatter)

    (mockFormatter.format _).verify(bond).once()
  }

  test("WithVariableParameters") {
    val australianFormat = mock[Formatter]

    (australianFormat.format _).expects(*).onCall { (s: String) => s"G'day $s" }.twice()

    Greetings.sayHello("Wendy", australianFormat)
    Greetings.sayHello("Gray", australianFormat)
  }

  test("WithParamAssertion") {
    val teamNatsu = Set("Natsu", "Lucy", "Happy", "Erza", "Gray", "Wendy", "Carla")
    val formatter = mock[Formatter]

    def assertTeamNatsu(s: String): Unit = {
      assert(teamNatsu.contains(s))
    }

    // argAssert fails early
    (formatter.format _).expects(argAssert(assertTeamNatsu _)).onCall { (s: String) => s"Yo $s" }.once()

    // 'where' verifies at the end of the test
    (formatter.format _).expects(where { (s: String) => teamNatsu contains(s) }).onCall { (s: String) => s"Yo $s" }.twice()

    Greetings.sayHello("Carla", formatter)
    Greetings.sayHello("Happy", formatter)
    Greetings.sayHello("Lucy", formatter)
  }

  test("WithBrokenGreeter") {
    val brokenFormatter = mock[Formatter]

    (brokenFormatter.format _).expects(*).throwing(new NullPointerException).anyNumberOfTimes()

    intercept[NullPointerException] {
      Greetings.sayHello("Erza", brokenFormatter)
    }
  }

  test("WithOrder") {
    val mockFormatter = mock[Formatter]

    inAnyOrder {
      (mockFormatter.format _).expects("Mr Bond").returns("Ah, Mr Bond. I've been expecting you")
      (mockFormatter.format _).expects("Natsu").returns("Not now Natsu!").atLeastTwice()
    }

    Greetings.sayHello("Natsu", mockFormatter)
    Greetings.sayHello("Natsu", mockFormatter)
    Greetings.sayHello("Mr Bond", mockFormatter)
    Greetings.sayHello("Natsu", mockFormatter)
  }

}

/*
  usually this would be in the production source folder
  In this case the source is in the test classe to
  illustrate a working example for easy reading
 */
object Greetings {
  trait Formatter { def format(s: String): String }
  object EnglishFormatter { def format(s: String): String = s"Hello $s" }
  object GermanFormatter { def format(s: String): String = s"Hallo $s" }
  object JapaneseFormatter { def format(s: String): String =  s"こんにちは $s" }

  def sayHello(name: String, formatter: Formatter): Unit = {
    println(formatter.format(name))
  }
}
