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

    (australianGreeting _).expects(*).onCall(s: String => s"G'day $s").twice()

    Greetings.sayHello("Wendy", australianGreeting)
    Greetings.sayHello("Gray", australianGreeting)
  }

  def testHelloWithParamAssertion(): Unit = {
    val someGreeting = mock[String => String]

    def assertTeamNatsu(s: String): Unit = {
      assert(Set("Natsu", "Lucy", "Happy", "Erza", "Gray", "Wendy", "Carla") contains(s))
    }

    (someGreeting _).expects(argAssert(assertTeamNatsu _)).onCall(s: String => s"Yo $s").twice()

    Greetings.sayHello("Natsu", someGreeting)
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
    val mockGreeter = stub[Greeter]

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
