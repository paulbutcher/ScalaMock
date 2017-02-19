---
layout: complex_article
title: User Guide - Choosing your mocking style
permalink: /user-guide/mocking_style/
---

# Choosing your mocking style 

ScalaMock supports two different mocking styles---*expectations-first* and *record-then-verify*. These styles can be mixed within a single test.

## Expectations-First Style

In the expectations-first style, expectations are set on mock objects before exercising the system under test. If these expectations are not met, the test fails.

### Mocking functions 

A mock function that supports this style is created with `mockFunction`. For example, to create a mock function taking a single `Int` argument and returning a `String`:

```scala
val m = mockFunction[Int, String]
```

### Mocking objects 

A mock object that supports expectations-first style is created with `mock`. For example, to create a mock that implements the `Heater` trait:

```scala
val heaterMock = mock[Heater]
```

Expectations can then be set using `expects`:

```scala
"CoffeeMachine" should "turn off the heater after coffee making is finished" in {
  val heaterMock = mock[Heater]
  val coffeeMachine = new CoffeeMachine(heaterMock)

  (heaterMock.isReady _).expects().returning(true)
  (heaterMock.setPowerState _).expects(PowerState.On)
  (heaterMock.setPowerState _).expects(PowerState.Off)
  
  coffeeMachine.makeCoffee()
}
```

In the above example `heaterMock.isReady` is expected be invoked without arguments (`.expects()`) and it will return `true` once invoked. The heater mock also expects to have its `setPowerState` method called twice, once with `PowerState.On` and once with `PowerState.Off`.  

Of course, in order to increase test readability you can define expectations, then exercise tested code, define subsequent expectations, continue executing system under test and so on. For example:

```scala
"CoffeeMachine" should "be able to prepare two coffees" in {

  val heaterMock = mock[Heater]
  val milkContainerMock = mock[MilkContainer]
  val coffeeMachine = new CoffeeMachine(heaterMock, milkContainerMock)
  
  // expectations for making a black coffee:
  
  (heaterMock.setPowerState _).expects(PowerState.On)
  (heaterMock.setPowerState _).expects(PowerState.Off)
  
  coffeeMachine.makeBlackCoffee()
  
  // expectations for making a coffee with milk:
  
  (milkContainerMock.isEmpty _).expects().returns(false)
  (milkContainerMock.fetchMilk _).expects(MilkPortions.SmallCoffee)
  
  (heaterMock.setPowerState _).expects(PowerState.On)
  (heaterMock.setPowerState _).expects(PowerState.Off)
  
  coffeeMachine.makeCoffeeWithMilk()
}
```

## Record-then-Verify (Mockito) Style

### Mocking functions 

In the record-then-verify style, expectations are verified after the system under test has executed. 

A stub function that supports this style is created with `stubFunction`. For example:

```scala
val m = stubFunction[Int, String]
```

### Mocking objects 

A stub object that supports the record-then-verify style is created with `stub`. For example:

```scala
val heaterStub = stub[Heater]
```

Return values that are used by the system under test can be set up by using `when` before running the tested system. Calls are verified using `verify`:

```
"CoffeeMachine" should "turn off the heater after coffee making is finished" in {
  val heaterStub = stub[Heater]
  val coffeeMachine = new CoffeeMachine(heaterStub)

  (heaterStub.isReady _).when().returns(true)

  coffeeMachine.makeCoffee()

  (heaterStub.setPowerState _).verify(PowerState.On)
  (heaterStub.setPowerState _).verify(PowerState.Off)
}
```

In the above example `heaterStub.isReady` will return `true` every time is called. This is different from the *Expectations-first* style example, where `heaterMock.isReady` is expected to be called exactly once. However, `setPowerState(PowerState.On)` is verified to be called exactly once because we did not use modifiers like `.anyNumberOfTimes()` or `.twice()`.

In the above example we don't care whether `heaterStub.isReady()` was called or not - we specify only return values. To verify that `isReady()` was called we would have to add third `verify` statement.

In both examples we verify that `setPowerState` is called with `PowerState.On` and `PowerState.Off` parameters. We do not enforce order in which this method is called. See the [Ordering](/user-guide/ordering) chapter for more details about this topic.

## Mixing mocking styles

You can mix different styles within a single test to get the best of each style. 

In the following example we are testing handling match results in simple Player vs Player game. The game provides two Leaderboards: *Top players* and *Top countries* based on the cumulative score of all players from given country. 

In the following test case:

* we test class `MatchResultObserver` that records `MatchResult`s and updates both `PlayerLeaderBoard` and `CountryLeaderBoard` so we use real `MatchResultObserver`
* the goal of this test case is to test that `CountryLeaderBoard` is updated correctly,
* we *stub* `PlayerDatabase` which provides details about players, because:
  * we don't want to test it in this test case (it has separate unit tests)
  * we want to provide player details without setting up database
  * we don't care how many times and in which order `PlayerDatabase.getPlayerById()` is called - it's up to the implementation
* we could either mock `CountryLeaderBoard` or stub it but we decided to *mock* it because:
  * the goal is to check `CountryLeaderBoard` is updated correctly so we want to write our expectation at the beginning of test (this is our style of writing tests)
  * we prefer to stub objects we don't have strict expectations and mock rest of them
* we *stub* `PlayerLeaderBoard` because we really don't care about it in this test case. We just have to provide it in `MatchResultObserver` constructor

### Example with mixed mocking styles

```scala
val loser = Player(id=111, nickname="Hans", country=Countries.Germany)
val winner = Player(id=222, nickname="Boris", country=Countries.Russia)

"MatchResultObserver" should "update PlayerLeaderBoard after finished match" in {
  val playerLeaderBoardStub = stub[PlayerLeaderBoard]
  val countryLeaderBoardMock = mock[CountryLeaderBoard]
  val playerDatabaseStub = stub[PlayerDatabase]
  
  // expectations
  (countryLeaderBoardMock.addVictoryForCountry _).expects(Countries.Russia)

  // defining stubs
  (playerDatabaseStub.getPlayerById _).when(loser.id).returns(loser)
  (playerDatabaseStub.getPlayerById _).when(winner.id).returns(winner)

  // run system under test
  val matchResultObserver = new MatchResultObserver(playerDatabaseStub, playerLeaderBoardStub, countryLeaderBoardMock)
  matchResultObserver.recordMatchResult(MatchResult(winner=winner.id, loser=loser.id))
}
```

### Example using Expectations-first style only

The above example using only *Expectations-first* style:

```scala
"MatchResultObserver" should "update PlayerLeaderBoard after finished match" in {
  val playerLeaderBoardMock = mock[PlayerLeaderBoard]
  val countryLeaderBoardMock = mock[CountryLeaderBoard]
  val playerDatabaseMock = mock[PlayerDatabase]
  
  (countryLeaderBoardMock.addVictoryForCountry _).expects(Countries.Russia)
  (playerLeaderBoardMock.addVictoryForPlayer _).expects(*).anyNumberOfTimes() // we don't care

  (playerDatabaseMock.getPlayerById _).expects(loser.id).returning(loser)
  (playerDatabaseMock.getPlayerById _).expects(winner.id).returning(winner)

  val matchResultObserver = new MatchResultObserver(playerDatabaseMock, playerLeaderBoardMock, countryLeaderBoardMock)
  matchResultObserver.recordMatchResult(MatchResult(winner=winner.id, loser=loser.id))
}
```

Please note that this time we check that `playerDatabaseMock.getPlayerById` is called exactly twice (once with `loser.id` and once `winner.id`).

### Example using Record-then-verify style only

The same example using only *Record-then-Verify* style:

```scala
"MatchResultObserver" should "update PlayerLeaderBoard after finished match" in {
  val playerLeaderBoardStub = stub[PlayerLeaderBoard]
  val countryLeaderBoardStub = stub[CountryLeaderBoard]
  val playerDatabaseStub = stub[PlayerDatabase]
  
  // defining stubs
  (playerDatabaseStub.getPlayerById _).when(loser.id).returns(loser)
  (playerDatabaseStub.getPlayerById _).when(winner.id).returns(winner)

  // run system under test
  val matchResultObserver = new MatchResultObserver(playerDatabaseStub, playerLeaderBoardStub, countryLeaderBoardStub)
  matchResultObserver.recordMatchResult(MatchResult(winner=winner.id, loser=loser.id))

  // expectations
  (countryLeaderBoardStub.addVictoryForCountry _).verify(Countries.Russia)
}
```

Please note that since we stubbed `CountryLeaderBoard`, we check that we add victory for Germany but we allow (since stubs are less strict than mocks) `countryLeaderBoardStub` to:

* have any other method invoked, for example `clearAllPointsForCountry(Countries.Russia)` or 
* have `addVictoryForCountry` called with different arguments, for example: `addVictoryForCountry(Countries.Germany)`.

We recommend that you:

* mock objects for which you have strict expectations and 
* stub objects for which you don't have strict expectations, for example because you prefer to make your tests more independent to trait implementations changes.

