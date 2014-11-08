---
layout: complex_article
title: User Guide - Features
permalink: /user-guide/features/
---

# Features

This is an overview of ScalaMock features. For more details about certain feature please refer appropriate User Guide chapter.

## Supported Scala features

ScalaMock provides fully type-safe support for **almost all Scala features**. This includes:

* mocking classes, traits and case classes
* mocking functions and operators
* mocking type parametrised and overloaded methods
* support for type constraints
* support for repeated parameters and named parameters
* mocking Java classes and interfaces

## Mocking features

### Argument Matching

```scala
// expect someMethod("foo", 42) to be called
(myMock.someMethod _).expects("foo", 42)  

// expect someMethod("foo", x) to be called for some integer x
(myMock.someMethod _).expects("foo", *)      

// expect someMethod("foo", x) to be called for some float x that is close to 42.0
(myMock.otherMethod _).expects("foo", ~42.0)

// expect sendMessage(receiver, message) for some receiver with name starting with "A"
(myMock.sendMessage _).expects(where { (receiver: Player, message: Message) => 
    receiver.name.startsWith("A")
}) 
```

### Ordering

```scala
// expect that machine is turned on before turning it off
inSequence {
  (machineMock.turnOn _).expects()
  (machineMock.turnOff _).expects()
}

// players can be fetched in any order
inAnyOrder {
  (databaseMock.getPlayerByName _).expects("Hans")
  (databaseMock.getPlayerByName _).expects("Boris")
}
```

### Call counts

```scala
// expect message to be sent twice
(myMock.sendMessage _).expects(*).twice

// expect message to be sent any number of times
(myMock.sendMessage _).expects(*).anyNumberOfTimes

// expect message to be sent no more than twice
(myMock.sendMessage _).expects(*).noMoreThanTwice
```

### Returning values

```scala
(databaseMock.getPlayerByName _).expects("Hans").returning(Player(name="Hans", country="Germany"))
(databaseMock.getPlayerByName _).expects("Boris").returning(Player(name="Hans", country="Russia"))
```

### Call handlers

```scala
// compute returned value
(fooMock.increment _) expects(*) onCall { arg: Int => arg + 1}
fooMock.increment(100) shouldBe 101

// throw computed exception
(fooMock.increment _) expects(*) onCall { arg: Int => throw new RuntimeException(arg) }
```
