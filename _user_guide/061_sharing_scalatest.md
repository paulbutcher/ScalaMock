---
layout: complex_article
title: User Guide - Sharing mocks and expectations in ScalaTest
permalink: /user-guide/sharing-scalatest/
---

# Sharing mocks and expectations in ScalaTest

Sometimes multiple test cases need to work with the same mocks (and more generally, the same fixtures: files, sockets, database connections, etc.). There are many techniques to avoid duplicating fixture code across test cases in ScalaTest, but ScalaMock recommends and officially supports these two:

* *isolated tests cases*: clean and simple, recommended when all test cases have the same or very similar fixtures
* *fixture contexts*: more flexible, recommended for complex test suites where a single set of fixtures does not fit all test cases

## Isolated test cases

If you mix the `org.scalatest.OneInstancePerTest` trait into a `Suite`, each test case will run in its own instance of the suite class. This means each test will get a fresh copy of the instance variables.

Then in the suite scope you can:

* declare instance variables (e.g., mocks) that will be used by multiple test cases and
* perform common test case setup (e.g., set up some mock expectations).

Because each test case has fresh instance variables, different test cases do not interfere with each other.

```scala
import org.scalatest.OneInstancePerTest
import org.scalamock.scalatest.proxy.MockFactory

// Please note that this test suite mixes in OneInstancePerTest
class CoffeeMachineTest extends FlatSpec with ShouldMatchers with OneInstancePerTest with MockFactory {
   // shared objects
   val waterContainerMock = mock[WaterContainer]
   val heaterMock = mock[Heater]
   val coffeeMachine = new CoffeeMachine(waterContainerMock, heaterMock)
   
   // you can set common expectations in the suite scope
   (heaterMock.isReady _).expects().returning(true)
   
   // and perform common test setup
   coffeeMachine.powerOn()
   
   "CoffeeMachine" should "not turn on the heater when the water container is empty" in {
       coffeeMachine.isOn shouldBe true
       (waterContainerMock.isEmpty _).expects().returning(true)
        
       // ...
       coffeeMachine.powerOff()
       coffeeMachine.isOn shouldBe false
   }
   
   it should "not turn on the heater when the water container is overfull" in {
       // each test case uses a separate, fresh test suite so the coffee machine is turned on
       // even if previous test case turned it off
       coffeeMachine.isOn shouldBe true
       // ...
   }
}
```

## Fixture contexts

You can also run each test case in a separate fixture context. Fixture contexts can be extended and combined. Since each test case uses a different instance of the fixture context, test cases do not interfere with each other and they have shared mocks and expectations.

```scala
class CoffeeMachineTest extends FlatSpec with ShouldMatchers with MockFactory {
   trait Test { // fixture context
       // shared objects
       val waterContainerMock = mock[WaterContainer]
       val heaterMock = mock[Heater]
       val coffeeMachine = new CoffeeMachine(waterContainerMock, heaterMock)
   
       // test setup
       coffeeMachine.powerOn()
   }
   
   "CoffeeMachine" should "not turn on the heater when the water container is empty" in new Test {
       coffeeMachine.isOn shouldBe true
       (waterContainerMock.isEmpty _).expects().returning(true)
       // ...
   }
   
   // you can extend and combine fixture-contexts
   trait OverfullWaterContainerTest extends Test {
       // you can set expectations and use mocks in the fixture-context
       (waterContainerMock.isOverfull _).expects().returning(true)
   
       // and define helper functions
       def sharedComplexLogic() {
         coffeeMachine.powerOff()
         // ...
       }
   }
   
   it should "not turn on the heater when the water container is overfull" in new OverfullWaterContainerTest {
       // ...
       sharedComplexLogic()
   }
}
```
