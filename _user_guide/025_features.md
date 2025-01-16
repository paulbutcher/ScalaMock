---
layout: complex_article
title: User Guide - Features
permalink: /user-guide/features/
---

# Features

This is an overview of ScalaMock features. For more details about a particular feature, please refer to the appropriate User Guide chapter.

ScalaMock provides fully type-safe support for **almost all Scala features**. This includes:

* mocking classes, traits and case classes
* mocking functions and operators
* mocking type parametrised and overloaded methods
* support for type constraints
* support for repeated parameters and named parameters
* mocking Java classes and interfaces

## ScalaMock 7

### Argument Matching

You can get arguments with which method was invoked using `calls` method.
It returns a list with one item per each method invokation.

Method with one arg:
```scala
myMock.someMethod.returns(_ => ())

myMock.someMethod("foo")
myMock.someMethod("bar")

myMock.someMethod.calls == List("foo", "bar") // true
```

Method with multiple args:

```scala
myMock.someOtherMethod.returns(_ => ())

myMock.someOtherMethod("foo", 1)
myMock.someOtherMethod("bar", 2)

myMock.someMethod.calls == List(("foo", 1), ("bar", 2)) // true
```

### Ordering

Basic support of call ordering is added via `CallLog`, `isBefore` and `isAfter` methods. `CallLog` should be declared before you create your stubs.

```scala
given CallLog = CallLog()

val foo = stub[Foo]
val bar = stub[Bar]

// setup stubs
foo.foo.returns(_ => 1)
bar.bar.returns(_ => 2)

// call methods (this is usually called internally in some other class)
foo.foo(1)
bar.bar(1)

foo.foo.isBefore(bar.bar) // true
bar.bar.isAfter(foo.foo) // true

```

### Call counts

You can get number of times method was invoked using `times` method.

```scala
myMock.someMethod.returns(_ => ())

myMock.someMethod("foo")
myMock.someMethod("bar")

myMock.someMethod.times == 2 // true
```

### Throwing exceptions

```scala
databaseMock.getPlayerByName.returns:
  case "George" => Player(123, "George")
  case _ => throw new NoSuchElementException
```

or just
```scala
databaseMock.getPlayerByName.returns(_ => throw new NoSuchElementException)
```

### Result based on arguments

One arg:

```scala
fooMock.increment.returns(arg => arg + 1)

fooMock.increment(100) shouldBe 101
```

Multiple args:
```scala
fooMock.sum.returns((x, y) => x + y)

fooMock.sum(1, 2) shouldBe 3
```

## ScalaMock

### Argument Matching

```scala
// expect someMethod("foo", 42) to be called
myMock.someMethod.expects("foo", 42).returns(()) 

// expect someMethod("foo", x) to be called for some integer x
myMock.someMethod.expects("foo", *).returns(())      

// expect someMethod("foo", x) to be called for some float x that is close to 42.0
myMock.otherMethod.expects("foo", ~42.0).returns(()) 

// expect sendMessage(receiver, message) for some receiver with name starting with "A"
myMock.sendMessage.expects(where { (receiver: Player, message: Message) => 
    receiver.name.startsWith("A")
}).returns(()) 
```

### Ordering

```scala
// expect that machine is turned on before turning it off
inSequence {
  machineMock.turnOn.expects().returns(()) 
  machineMock.turnOff.expects().returns(()) 
}

// players can be fetched in any order
inAnyOrder {
  databaseMock.getPlayerByName.expects("Hans").returns(()) 
  databaseMock.getPlayerByName.expects("Boris").returns(()) 
}
```

### Call counts

```scala
// expect message to be sent twice
myMock.sendMessage.expects(*).twice

// expect message to be sent any number of times
myMock.sendMessage.expects(*).anyNumberOfTimes

// expect message to be sent no more than twice
myMock.sendMessage.expects(*).noMoreThanTwice
```

### Returning values

```scala
databaseMock.getPlayerByName.expects("Hans").returning(Player(name="Hans", country="Germany"))
databaseMock.getPlayerByName.expects("Boris").returning(Player(name="Hans", country="Russia"))
```

### Throwing exceptions

```scala
databaseMock.getPlayerByName.expects("George").throwing(new NoSuchElementException)
```

### Call handlers

```scala
// compute returned value
fooMock.increment.expects(*).onCall { arg: Int => arg + 1}
fooMock.increment(100) shouldBe 101

// throw computed exception
fooMock.increment.expects(*).onCall { arg: Int => throw new RuntimeException(arg) }
```
