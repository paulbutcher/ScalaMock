---
layout: complex_article
title: User Guide - Sharing mocks and expectations in Specs2
permalink: /user-guide/sharing-specs2/
---

# Sharing mocks and expectations in Specs2

To use ScalaMock in Specs2 tests you can either:

* mix `Specification` with `org.scalamock.specs2.IsolatedMockFactory` to get *isolated test cases* or
* run each test case in separate *fixture context* - `org.scalamock.specs2.MockContext`

## Fixture contexts

*Fixture context* instances should be created per test-case basis and implement `MockContext` trait.

Fixture contexts are more flexible and are recommended for complex test suites where single set of fixtures does not fit all test cases.

### Basic usage 

For simple test cases it's enough to run each test case in new `MockContext` scope.

```scala
class BasicCoffeeMachineTest extends Specification {

   "CoffeeMachine" should {
        "not turn on the heater when the water container is empty" in new MockContext {
            val waterContainerMock = mock[WaterContainer]
            (waterContainerMock.isEmpty _).expects().returning(true)
            // ...
        }

        "not turn on the heater when the water container is overfull" in new MockContext {
            val waterContainerMock = mock[WaterContainer]
            // ...
        }
   }
}
```

### Complex fixture contexts

When multiple test cases need to work with the same mocks (and more generally - the same fixtures: files, sockets, database connections, etc.) you can use fixture contexts that extends `MockContext` trait.

```scala
class CoffeeMachineTest extends Specification {

  trait Test extends MockContext { // fixture context
    // shared objects
    val waterContainerMock = mock[WaterContainer]
    val heaterMock = mock[Heater]
    val coffeeMachine = new CoffeeMachine(waterContainerMock, heaterMock)

    // test setup
    coffeeMachine.powerOn()
  }

  // you can extend and combine fixture-contexts
  trait OverfullWaterContainerTest extends Test {
    // you can set expectations and use mocks in fixture-context
    (waterContainerMock.isOverfull _).expects().returning(true)

    // and define helper functions
    def complexLogic() {
        coffeeMachine.powerOff()
        // ...
    }
  }

  "CoffeeMachine" should {
     "not turn on the heater when the water container is empty" in new MockContext {
         val heaterMock = mock[Heater]
         val waterContainerMock = mock[WaterContainer]
         val coffeeMachine = new CoffeeMachine(waterContainerMock, heaterMock)
         (waterContainerMock.isEmpty _).expects().returning(true)
         // ...
     }

     "not turn on the heater when the water container is overfull" in new OverfullWaterContainerTest {
         // ...
         complexLogic()
     }
  }
}
```

## Isolated test cases

Using *Isolated tests cases* is a clean and simple way to share mocks and fixture by all test cases. This technique is recommended when all test cases use the same or very similar fixtures.

`IsolatedMockFactory` can be mixed into a [Specs2](http://etorreborre.github.com/specs2/) `Specification` to provide mocking support. Please note that this causes to run whole suite in isolated mode.

```scala
class IsolatedCoffeeMachineTest extends Specification with IsolatedMockFactory {
  
  // shared objects
  val waterContainerMock = mock[WaterContainer]
  val heaterMock = mock[Heater]
  val coffeeMachine = new CoffeeMachine(waterContainerMock, heaterMock)

  // you can set common expectations in suite scope
  (heaterMock.isReady _).expects().returning(true)

  // test setup
  coffeeMachine.powerOn()

  "CoffeeMachine" should {
      "not turn on the heater when the water container is empty" in {
          coffeeMachine.isOn must_== true
          // ...
          coffeeMachine.powerOff()
          coffeeMachine.isOn must_== false
      }

      "not turn on the heater when the water container is overfull" in {
          // each test case uses separate, fresh Suite so the coffee machine is turned on
          coffeeMachine.isOn must_== true
          // ...
      }
  }
}
```
