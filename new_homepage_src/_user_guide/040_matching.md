---
layout: complex_article
title: User Guide - Argument matching
permalink: /user-guide/matching/
---

# Argument matching

ScalaMock supports three types of generalised matching: 

* wildcards
* epsilon matching
* predicate matching

Wildcard values are specified with an `*` (asterisk). For example:

```scala
val mockedFunction = mockFunction[String, Object, Unit] // (String, Object) => Unit
mockedFunction expects ("foo", *)
```
will match any of the following:

```scala
mockedFunction("foo", 42)
mockedFunction("foo", 1.0)
mockedFunction("foo", null)
```

## Epsilon matching

Epsilon matching is useful when dealing with floating point values. An epsilon match is specified with the `~` (tilde) operator:

```scala
mockedFunction expects (~42.0)
```

will match:

```scala
mockedFunction(42.0)
mockedFunction(42.0001)
mockedFunction(41.9999)
```

but will not match:

```scala
mockedFunction(43.0)
mockedFunction(42.1)
```

## Predicate matching

More complicated argument matching can be implemented by using `where` to pass a predicate.

```scala
def where[Arg1](predicate: (Arg1) => Boolean)
def where[Arg1, Arg2](predicate: (Arg1, Arg2) => Boolean)
def where[Arg1, Arg2, Arg3](predicate: (Arg1, Arg2, Arg3) => Boolean)
...
```

### Example 1

In this example we will use the following `PlayerLeaderBoard` interface.

```scala
case class Player(id: Long, name: String, emailAddress: String, country: String)

trait PlayerLeaderBoard {
    def addPointsForPlayer(player: Player, points: Int): Unit
}
```

Now imagine that we want to set expectation that `addPointsForPlayer` is called with:

* `points` equal to 100 and
* `player` who can have any `name`, any `emailAddress` and come from any `country` as long its `id` is 789

To achieve that in ScalaMock we can use where with appropriate predicate:

```
(leaderBoardMock.addPointsForPlayer _) expects (where {
  (player: Player, points: Int) => player.id == 789 && points == 100
}) 
```

### Example 2
```scala
mockedFunction = mockFunction[Double, Double, Unit] // (Double, Double) => Unit
mockedFunction expects (where { _ < _ }) // expects that arg1 < arg2 
```



