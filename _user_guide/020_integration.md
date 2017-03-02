---
layout: complex_article
title: User Guide - Integration with testing frameworks
permalink: /user-guide/integration/
---

# Integration with testing frameworks

ScalaMock can be used in tests written using the [ScalaTest](http://scalatest.org) or [Specs2](http://etorreborre.github.io/specs2/) frameworks.

## ScalaTest 

To provide mocking support in ScalaTest suites just mix your suite with `org.scalamock.scalatest.MockFactory`:

```scala
import org.scalatest.FlatSpec
import org.scalatest.ShouldMatchers
import org.scalamock.scalatest.MockFactory

class CoffeeMachineTest extends FlatSpec with ShouldMatchers with MockFactory {

  "CoffeeMachine" should "not turn on the heater when the water container is empty" in {
      val waterContainerMock = mock[WaterContainer]
      (waterContainerMock.isEmpty _).expects().returning(true)
      // ...
  }
}
```

When testing `Future`s with `AsyncTestSuite`s (eg. `AsyncFlatSpec`), mix in `org.scalamock.scalatest.AsyncMockFactory` instead:

```scala
class ExchangeRateListingTest extends AsyncFlatSpec with AsyncMockFactory {

  val eur = Currency(id = "EUR", valueToUSD = 1.0531, change = -0.0016)
  val gpb = Currency(id = "GPB", valueToUSD = 1.2280, change = -0.0012)
  val aud = Currency(id = "AUD", valueToUSD = 0.7656, change = -0.0024)

  "ExchangeRateListing" should "eventually return the exchange rate between passed Currencies when getExchangeRate is invoked" in {
      val currencyDatabaseStub = stub[CurrencyDatabase]
      (currencyDatabaseStub.getCurrency _).when(eur.id).returns(eur)
      (currencyDatabaseStub.getCurrency _).when(gpb.id).returns(gpb)
      (currencyDatabaseStub.getCurrency _).when(aud.id).returns(aud)
      
      val listing = new ExchangeRateListing(currencyDatabaseStub)
      
      val future: Future[Double] = listing.getExchangeRate(eur.id, gpb.id)
      
      future map (exchangeRate => assert(exchangeRate == eur.valueToUSD / gpb.valueToUSD))
  }
}
```

## Specs2

To use ScalaMock in Specs2 tests you should run each test case in a separate fixture context that implements `org.scalamock.specs2.MockContext`:


```scala
import org.specs2.mutable.Specification
import org.scalamock.specs2.MockContext

class BasicCoffeeMachineTest extends Specification {

  "CoffeeMachine" should {
    "not turn on the heater when the water container is empty" in new MockContext {
        val waterContainerMock = mock[WaterContainer]
        (waterContainerMock.isEmpty _).expects().returning(true)
        // ...
    }
}
```
There are other ways of using ScalaMock in Specs2 tests - please check [Sharing mocks and expectations](/user-guide/sharing-specs2/) chapter for more details.

