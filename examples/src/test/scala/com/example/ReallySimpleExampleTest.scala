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

import com.example.Greetings.Greeter
import org.scalamock.scalatest.MockFactory
import org.scalatest.funsuite.AnyFunSuite

class ReallySimpleExampleTest extends AnyFunSuite with MockFactory {
  def testHello(): Unit = {
    val mockGreeter = mock[Greeter]

    (mockGreeter _).expects("Mr Bond").returning("Ah, Mr Bond. I've been expecting you").once()

    Greetings.sayHello("Mr Bond", mockGreeter)
  }

  def testHelloMockitoStyle(): Unit = {
    val mockGreeter = stub[Greeter]
    val bond = "Mr Bond"

    (mockGreeter _).when(bond).returns("Ah, Mr Bond. I've been expecting you")

    Greetings.sayHello(bond, mockGreeter)

    (mockGreeter _).verify(bond).once()
  }

  def testHelloWithVariableParameters(): Unit = {
    val australianGreeting = mock[Greeter]

    (australianGreeting _).expects(*).onCall { s: String => s"G'day $s" }.twice()

    Greetings.sayHello("Wendy", australianGreeting)
    Greetings.sayHello("Gray", australianGreeting)
  }

  def testHelloWithParamAssertion(): Unit = {
    val teamNatsu = Set("Natsu", "Lucy", "Happy", "Erza", "Gray", "Wendy", "Carla")
    val someGreeting = mock[String => String]

    def assertTeamNatsu(s: String): Unit = {
      assert(teamNatsu.contains(s))
    }

    // argAssert fails early
    (someGreeting _).expects(argAssert(assertTeamNatsu _)).onCall { s: String => s"Yo $s" }.once()

    // 'where' verifies at the end of the test
    val w = where(teamNatsu contains(_: String))
    (someGreeting _).expects(where { s: String => teamNatsu contains(s) }).onCall { s: String => s"Yo $s" }.twice()

    Greetings.sayHello("Carla", someGreeting)
    Greetings.sayHello("Happy", someGreeting)
    Greetings.sayHello("Lucy", someGreeting)
  }

  def testHelloWithBrokenGreeter(): Unit = {
    val brokenGreeter = mock[String => String]

    (brokenGreeter _).expects(*).throwing(new NullPointerException).anyNumberOfTimes()

    intercept[NullPointerException] {
      Greetings.sayHello("Erza", brokenGreeter)
    }
  }

  def testHelloWithOrder(): Unit = {
    val mockGreeter = mock[Greeter]

    inAnyOrder {
      (mockGreeter _).when("Mr Bond").returns("Ah, Mr Bond. I've been expecting you")
      (mockGreeter _).when("Natsu").returns("Not now Natsu!").atLeastTwice()
    }

    Greetings.sayHello("Natsu", mockGreeter)
    Greetings.sayHello("Natsu", mockGreeter)
    Greetings.sayHello("Mr Bond", mockGreeter)
    Greetings.sayHello("Natsu", mockGreeter)
  }

}

/*
  usually this would be in the production source folder
  In this case the source is in the test classe to
  illustrate a working example for easy reading
 */
object Greetings {
  type Greeter = String => String
  def EnglishGreeter(name: String) = s"Hello $name"
  def GermanGreeter(name: String) = s"Hallo $name"
  def JapaneseGreeter(name: String) = s"こんにちは $name"

  def sayHello(name: String, formatter: Greeter): Unit = {
    println(formatter(name))
  }
}
