---
layout: complex_article
title: User Guide - Advanced topics
permalink: /user-guide/advanced_topics/
---

# Advanced topics

## Mocking overloaded, curried and polymorphic methods

Overloaded, curried and polymorphic methods can be mocked by specifying either argument types or type parameters.

### Example 1 - Overloaded methods

```scala
trait Foo {
  def overloaded(x: Int): String
  def overloaded(x: String): String
  def overloaded[T](x: T): String
}

val fooMock = mock[Foo]

(fooMock.overloaded(_: Int)).expects(10)
(fooMock.overloaded(_: String)).expects("foo")
(fooMock.overloaded[Double] _).expects(1.23)
```

You may also prefer to use slightly different syntax:

```
(fooMock.overloaded _: Int => String).expects(10)  // or
(fooMock.overloaded _: Int => String) expects (10) // or
(fooMock.overloaded(_: Int)) expects (10)
```

### Example 2 - Polymorphic methods

```scala
trait Foo {
  def polymorphic[T](x: List[T]): String
}

val fooMock = mock[Foo]

(fooMock.polymorphic(_: List[Int])).expects(List(1, 2, 3))            // or
(fooMock.polymorphic[Int] _).expects(List(1, 2, 3))                   // or
(fooMock.polymorphic _ : List[Int] => String).expects(List(1, 2, 3))
```

### Example 3 - Curried methods 

```scala
trait Foo {
  def curried(x: Int)(y: Double): String
}

val fooMock = mock[Foo]

(fooMock.curried(_: Int)(_: Double)).expects(10, 1.23)
```

### Example 4 - Methods with implicit parameters 

This case is very similar to curried methods - all you need to do is to help scala compiler know that `memcachedMock.get _` should be converted to `MockFunction2`. For example:

```scala
class Codec()

trait Memcached {
  def get(key: String)(implicit codec: Codec): Option[Int]
}

val memcachedMock = mock[Memcached]

implicit val codec = new Codec
(memcachedMock.get(_ : String)(_ : Codec)).expects("some_key", *).returning(Some(123))
```

### Example 5 - Repeated parameters

Repeated parameters are represented as a Seq. For example, given:

```scala
trait Foo {
  def takesRepeatedParameter(x: Int, ys: String*)
}
```
you can set an expectation with:

```scala
(fooMock.takesRepeatedParameter _).expects(42, Seq("red", "green", "blue"))
```

## Returning values (onCall)

By default mocks and stubs return `null`. You can return predefined value using `returning()` method (or `returns()` in case of stubs). When returned value depends on function arguments, you can return the computed value (or throw a computed exception) with `onCall()`.

```scala
trait Foo {
    def increment(a: Int): Int
}

val fooMock = mock[Foo]

(fooMock.increment _) expects(12) returning(13)
fooMock.increment(12) shouldBe 13 

(fooMock.increment _) expects(*) onCall { arg: Int => arg + 1}
fooMock.increment(100) shouldBe 101

(fooMock.increment _) expects(*) onCall { arg: Int => throw new RuntimeException("message") }
intercept[RuntimeException] { fooMock.increment(0) }
```
```scala
val mockIncrement = mockFunction[Int, Int]
mockIncrement expects (*) onCall { arg: Int => arg + 1 }
mockIncrement(10) shouldBe  11 
```

## Call count

By default, mocks and stubs expect exactly one call. Alternative constraints can be set with `repeat()`:

```scala
mockedFunction.expects(42).returns(42).repeat(3 to 7)
mockedFunction expects (3) repeat 10
```

There are various aliases for common expectations and styles:

```scala
val mockedFunction1 = mockFunction[Int, String]
val mockedFunction2 = mockFunction[Int, String]
val mockedFunction3 = mockFunction[Int, String]
val mockedFunction4 = mockFunction[Int, String]
val mockedFunction5 = mockFunction[Int, String]

mockedFunction1.expects(1).returning("foo").once
mockedFunction2.expects(2).returning("foo").noMoreThanTwice
mockedFunction3.expects(3).returning("foo").repeated(3).times
mockedFunction4.expects(4).returning("foo").repeat(1 to 2)
mockedFunction5.expects(5).returning("foo").repeat(2)
mockedFunction5.expects(6).returning("foo").never()

mockedFunction1(1)

//mockedFunction2(2) - not called

mockedFunction3(3)
mockedFunction3(3)
mockedFunction3(3)

mockedFunction4(4)
mockedFunction4(4)

mockedFunction5(5)
mockedFunction5(5)

//mockedFunction6(6) - not called
```

For a full list, see `org.scalamock.CallHandler`.

## Exceptions

Instead of a return value, mocks and stubs can be instructed to throw. This can be achieved either by throwing exception in `onCall` handler or by using `throws` method.


### Example 1 - throws method

```scala
trait Foo {
  def increment(a: Int): Int
}

val fooMock = mock[Foo]

(fooMock.increment _) expects (5) throws new RuntimeException("message")

intercept[RuntimeException] { fooMock.increment(5) }
```

### Example 2 - throwing from `onCall` handler
```scala
(fooMock.increment _) expects(*) onCall { arg: Int => 
  if (arg == 0) 
      throw new RuntimeException("message") 
  else
      arg + 1
}

intercept[RuntimeException] { fooMock.increment(0) }
```

## Partial Functions

### Example 1

```scala    
val m = mockFunction[Int, String]
val mp = new PartialFunction[Int, String] {
  def isDefinedAt(x: Int) = true
  def apply(v1: Int) = m(v1)
}

m expects 42 returning "foo" once()

mp(42) shouldBe "foo"
```

## Raw types

### Example 1

Try this solution if you get this error:

`error: could not find implicit value for evidence parameter of type org.scalamock.Defaultable[SomeType]`

e.g. like below for `Enumeration` and `Map`

```java
public interface RawTypeInterface {
    java.util.Enumeration foo();
    java.util.Map bar();
}
```

```scala
"mocking a java method with raw type" should "work" in {
  implicit val d = new Defaultable[java.util.Enumeration[_]] {
    override val default = null
  }
  implicit val d2 = new Defaultable[java.util.Map[_, _]] {
    override val default = null
  }
  
  val mockedRaw = mock[RawTypeInterface]
}
```

## Log Calls

### Example 1

```scala
inAnyOrderWithLogging { // or inSequenceWithLogging
  someMock.foo _ expects() returning 42 anyNumberOfTimes()
}
```

This will print all invocations of call handlers and verifiers, with the corresponding calls.
