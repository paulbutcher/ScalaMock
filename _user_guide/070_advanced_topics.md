---
layout: complex_article
title: User Guide - Advanced topics
permalink: /user-guide/advanced_topics/
---

# Advanced topics

If you are looking for plain old scalamock examples, please refer to [ScalaMock](./070_advanced_topics.md#scalamock) section.

## ScalaMock 7

### Mocking 0-parameter method

```scala
trait Foo:
  def bar(): Int
  def buz: Int

val fooMock = mock[Foo]

fooMock.bar().returns(0)
fooMock.buz.returns(0)
```

### Overloaded methods

```scala
trait Foo:
  def overloaded(x: Int): String
  def overloaded(x: String): String
  def overloaded[T](x: T): String


val fooMock = mock[Foo]

(fooMock.overloaded(_: Int)).returns(_ => "")
(fooMock.overloaded(_: String)).returns(_ => "")
fooMock.overloaded[Double].returns(_ => "")
```

Also you can inline it to use later on:

```scala

inline def overloadedInt = fooMock.overloaded(_: Int)

overloadedInt.returns(_ => "")
overloadedInt.times

```

### Polymorphic methods

```scala
trait Foo:
  def polymorphic[T](x: List[T]): String


val fooMock = mock[Foo]

fooMock.polymorphic[Int].returns:
  case List(1, 2, 3) => ""
  case _ => "0"

// also you can specify argument types if compiler somehow can't resolve a method
(fooMock.polymorphic[Int](_: List[Int])).returns:
  case List(1, 2, 3) => ""
  case _ => "0"
```

### Curried methods 

```scala
trait Foo:
  def curried(x: Int)(y: Double): String


val fooMock = mock[Foo]

(fooMock.curried(_: Int)(_: Double)).returns:
  case (10, 1.23) => ""

// this also can be inlined and used

inline def curried = fooMock.curried(_: Int)(_: Double)

curried.returns(_ => "")

```

### Methods with using parameters 

```scala
class Codec()

trait Memcached:
  def get(key: String)(using Codec): Option[Int]

val memcachedMock = mock[Memcached]

given Codec = new Codec
(memcachedMock.get(_ : String)(using _: Codec)).returns((x, y) => Some(123))

// this also can be inlined and used

inline def withUsingParams = memcachedMock.get(_ : String)(using _: Codec)

withUsingParams.returns(_ => Some(123))
```

### Repeated parameters

Repeated parameters are represented as a Seq. For example, given:

```scala
trait Foo:
  def takesRepeatedParameter(x: Int, ys: String*): Unit
  def takesRepeatedParamCurried(x: Int*)(y: String*): Unit
```
you can set an expectation with:

```scala
fooMock.takesRepeatedParameter.expects(42, Seq("red", "green", "blue")).returns(())

(fooMock.takesRepeatedParamCurried((_: Seq[Int])*)((_: Seq[String])*))
  .returns((seq1, seq2) => ())

// this also can be inlined and used later on

inline def multipleRepeatedCurried = fooMock
  .takesRepeatedParamCurried((_: Seq[Int])*)((_: Seq[String])*)

multipleRepeatedCurried.returns((seq1, seq2) => ())

```

## ScalaMock

### Mocking 0-parameter method

```scala
trait Foo:
  def bar(): Int
  def buz: Int

val fooMock = mock[Foo]

(() => fooMock.bar()).expects(10).returns(0)
(() => fooMock.buz).expects(10).returns(0)
```

### Mocking overloaded, curried and polymorphic methods

Overloaded, curried and polymorphic methods can be mocked by specifying either argument types or type parameters.

#### Overloaded methods

```scala
trait Foo:
  def overloaded(x: Int): String
  def overloaded(x: String): String
  def overloaded[T](x: T): String


val fooMock = mock[Foo]

(fooMock.overloaded(_: Int)).expects(10).returns("")
(fooMock.overloaded(_: String)).expects("foo").returns("")
fooMock.overloaded[Double].expects(1.23).returns("")
```

#### Polymorphic methods

```scala
trait Foo:
  def polymorphic[T](x: List[T]): String


val fooMock = mock[Foo]

fooMock.polymorphic[Int].expects(List(1, 2, 3)).returns("")

// also you can specify argument types if compiler somehow can't resolve a method
(fooMock.polymorphic[Int](_: List[Int])).expects(List(1, 2, 3)).returns("") 
```

#### Curried methods 

```scala
trait Foo:
  def curried(x: Int)(y: Double): String


val fooMock = mock[Foo]

(fooMock.curried(_: Int)(_: Double)).expects(10, 1.23).returns("")
```

#### Methods with implicit parameters 

This case is very similar to curried methods. All you need to do is to help the Scala compiler know that `memcachedMock.get _` should be converted to `MockFunction2`. For example:

```scala
class Codec()

trait Memcached:
  def get(key: String)(using Codec): Option[Int]

val memcachedMock = mock[Memcached]

given Codec = new Codec
(memcachedMock.get(_ : String)(using _: Codec)).expects("some_key", *).returning(Some(123))
```

#### Repeated parameters

Repeated parameters are represented as a Seq. For example, given:

```scala
trait Foo:
  def takesRepeatedParameter(x: Int, ys: String*): Unit
```
you can set an expectation with:

```scala
fooMock.takesRepeatedParameter.expects(42, Seq("red", "green", "blue")).returns(())
```

### Returning values (onCall)

You can return predefined value using the `returning()` method, or in the case of stubs, `returns()`. When the returned value depends on function arguments, you can return the computed value (or throw a computed exception) with `onCall()`. For example:

```scala
trait Foo:
    def increment(a: Int): Int


val fooMock = mock[Foo]

fooMock.increment.expects(12).returning(13)
fooMock.increment(12) shouldBe 13 

fooMock.increment.expects(*).onCall { arg: Int => arg + 1}
fooMock.increment(100) shouldBe 101

fooMock.increment.expects(*).onCall { arg: Int => throw new RuntimeException("message") }
intercept[RuntimeException] { fooMock.increment(0) }
```

```scala
val mockIncrement = mockFunction[Int, Int]
mockIncrement.expects(*).onCall { arg: Int => arg + 1 }
mockIncrement(10) shouldBe  11 
```

### Call count

By default, mocks expect exactly one call while stubs allow any number of calls.
For stubs, an exact number of calls can be specified in the verification phase.
For mocks, alternative constraints can be set with `repeat()`:

```scala
mockedFunction.expects(42).returns(42).repeat(3 to 7)
mockedFunction.expects(3).repeat(10)
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

### Exceptions

Instead of a return value, mocks and stubs can be instructed to throw an exception. This can be achieved either by throwing exception in the `onCall` handler or by using the `throws` method.


#### throws method

```scala
trait Foo:
  def increment(a: Int): Int


val fooMock = mock[Foo]

fooMock.increment.expects(5).throws(new RuntimeException("message"))

intercept[RuntimeException] { fooMock.increment(5) }
```

####  throwing from the `onCall` handler

```scala
fooMock.increment.expects(*).onCall { arg: Int => 
  if (arg == 0) 
      throw new RuntimeException("message") 
  else
      arg + 1
}

intercept[RuntimeException] { fooMock.increment(0) }
```

### Partial Functions

#### Example 1

```scala    
val m = mockFunction[Int, String]
val mp = new PartialFunction[Int, String]:
  def isDefinedAt(x: Int) = true
  def apply(v1: Int) = m(v1)


m.expects(42).returning("foo").once()

mp(42) shouldBe "foo"
```

### Raw types

#### Example 1

Try this solution if you get this error:

`error: could not find implicit value for evidence parameter of type org.scalamock.Defaultable[SomeType]`

e.g., such as the following example for `Enumeration` and `Map`

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

### Log Calls

#### Example 1

```scala
inAnyOrderWithLogging { // or inSequenceWithLogging
  someMock.foo.expects().returning(42).anyNumberOfTimes()
}
```

This will print all invocations of call handlers and verifiers with the corresponding calls.

### Argument Capture

(since ScalaMock 4.2.0)
Using the Capture feature in `org.scalamock.matchers.ArgCapture`, it is easy and convenient to allow wildcard matches but assert on the results later on.
It is possible to store either a single value in a `CaptureOne`, or a `Seq` or values with a `CaptureAll`. Note that the call to `.value` will throw if nothing was captured. Also, the `CaptureOne` will only keep the last value captured, should it be invoked multiple times.

```scala
  "ScalaMock" can "capture the arguments of mocks - capture one" in {
    val m = mock[TestTrait]
    val c1 = CaptureOne[Int]()

    m.oneParam.expects(capture(c1)).returns(()).once()
    m.oneParam(42)
    c1.value should be (42)
  }

  "ScalaMock" can "capture the arguments of mocks - capture all" in {
    val m = mock[TestTrait]
    val c = CaptureAll[Int]()

    m.oneParam.expects(capture(c)).returns(()).repeat(3)
    m.oneParam(99)
    m.oneParam(17)
    m.oneParam(583)
    c.value should be (583)
    c.values should be (Seq(99, 17, 583))
  }
```

### Using ScalaMock without ScalaTest/Specs2

You just need to implement your own subtype of `MockFactoryBase`. Technically you can adapt ScalaMock to be used inside JUnit, µTest, etc this way, or any other framework really.

```scala
import org.scalamock.MockFactoryBase
import org.scalamock.clazz.Mock

object NoScalaTestExample extends Mock:
  trait Cat:
    def meow(): Unit
    def isHungry: Boolean


  class MyMockFactoryBase extends MockFactoryBase:
    override type ExpectationException = Exception
    override protected def newExpectationException(message: String, methodName: Option[Symbol]): Exception =
      throw new Exception(s"$message, $methodName")

    def verifyAll(): Unit = withExpectations(() => ())
  

  implicit var mc: MyMockFactoryBase = _
  var cat: Cat = _

  def main(args: Array[String]): Unit =
    // given: I have a mock context
    mc = new MyMockFactoryBase
    // and am mocking a cat
    cat = mc.mock[Cat]
    // and the cat meows
    cat.meow.expects().returns(()).once()
    // and the cat is always hungry
    cat.isHungry.expects().returning(true).anyNumberOfTimes()

    // then the cat needs feeding
    assert(cat.isHungry)

    // and the mock verifies
    mc.verifyAll()
```
