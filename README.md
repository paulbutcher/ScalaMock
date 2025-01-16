![GitHub Release](https://img.shields.io/github/v/release/scalamock/scalamock?color=blue])

# What is ScalaMock? 

ScalaMock is native Scala mocking framework.

---
Mocking is a software testing technique that involves creating simulated components,
known as mocks or stubs, to mimic the behavior of real components in a system.
The purpose of such testing is to isolate and evaluate specific parts of a software application 
by replacing real dependencies with mocks or stubs.

---

Full documentation is on official website: [scalamock.org](https://scalamock.org/)

## ScalaMock 7 

### Prerequisites
ScalaMock 7 works only with scala 3.4+
More details about it here https://github.com/ScalaMock/ScalaMock/issues/567, but shortly:
1. scalamock 7 uses `TupledFunction` and it currently can't be used without `-experimental` compiler flag, which **won't be** backported to LTS 3.3
2. scalamock 6 takes anvantage of scala compiler bug allowing to omit annotating everything as `@experimental`

```scala
scalaVersion := "3.4.3" // or higher
Test / scalacOptions += "-experimental"
```

### Alternative experimental API
Offers you:

1. Concise and powerful syntax with no complexity overhead
2. Data based approach. You can get arguments or number of times method was called
3. No exceptions thrown
4. Support for functional effects like `ZIO` or `cats-effect IO`

### Dependencies

```scala
libraryDependencies ++= Seq(
  // core module
  "org.scalamock" %% "scalamock" % "<latest version in badge>",
  // zio integration
  "org.scalamock" %% "scalamock-zio" % "latest version in badge",
  // cats-effect integration
  "org.scalamock" %% "scalamock-cats-effect" % "latest version in badge"
)
```

### QuickStart

Quickstart is [here](https://scalamock.org/quick-start/).

ZIO/CE integration is [here](https://scalamock.org/user-guide/integration/).

## ScalaMock

### Expectations-First Style

```scala
test("drawline interaction with turtle") {
  // Create mock Turtle object
  val m = mock[Turtle]
  
  // Set expectations
  m.setPosition.expects(10.0, 10.0).returns(10.0, 10.0)
  m.forward.expects(5.0).returns(())
  m.getPosition.expects().returns(15.0, 10.0)

  // Exercise System Under Test
  drawLine(m, (10.0, 10.0), (15.0, 10.0))
}
```

### Record-then-Verify (Mockito) Style

```scala
test("drawline interaction with turtle") {
  // Create stub Turtle
  val m = stub[Turtle]
  
  // Setup return values
  m.getPosition.when().returns(15.0, 10.0)

  // Exercise System Under Test
  drawLine(m, (10.0, 10.0), (15.0, 10.0))

  // Verify expectations met
  m.setPosition.verify(10.0, 10.0)
  m.forward.verify(5.0)
}
```

A more complete example is on our [Quickstart](http://scalamock.org/quick-start/) page.

## Features

* Fully typesafe
* Full support for Scala features such as:
  * Polymorphic (type parameterised) methods
  * Operators (methods with symbolic names)
  * Overloaded methods
  * Type constraints
* ScalaTest and Specs2 integration
* Mock and Stub support
* Macro Mocks and JVM Proxy Mocks
* Scala.js support
* built for Scala 2.12, 2.13, 3
* Scala 2.10 support was included up to ScalaMock 4.2.0
* Scala 2.11 support was included up to ScalaMock 5.2.0
* Scala 2.12 and 2.13 support was included up to ScalaMock 6.1.1

## Using ScalaMock

Artefacts are published to Maven Central and Sonatype OSS.

For ScalaTest, to use ScalaMock in your Tests, add the following to your `build.sbt`:

```scala
libraryDependencies += Seq(
  "org.scalamock" %% "scalamock" % "5.2.0" % Test,
  "org.scalatest" %% "scalatest" % "3.2.0" % Test
)
```

## Scala 3 Migration Notes

1. Type should be specified for methods with by-name parameters
```scala 3
trait TestTrait:
  def byNameParam(x: => Int): String

val t = mock[TestTrait]

// this one no longer compiles
(t.byNameParam _).expects(*).returns("")

// this one should be used instead
(t.byNameParam(_: Int)).expects(*).returns("")
``` 

2.    
* Not initialized vars are not supported anymore, use `scala.compiletime.uninitialized` instead
* Vars are **not mockable** anymore

```scala 3
trait X:
  var y: Int  // No longer compiles
  
  var y: Int = scala.compile.uninitialized // Should be used instead
```


 Mocking of non-abstract java classes is not available without workaround

```java
public class JavaClass {
    public int simpleMethod(String b) { return 4; }
}

```

```scala 3
val m = mock[JavaClass] // No longer compiles

class JavaClassExtended extends JavaClass

val mm = mock[JavaClassExtended] // should be used instead
```

## Documentation

For usage in Maven or Gradle, integration with Specs2, and more example examples see the [User Guide](http://scalamock.org/user-guide/)

## Acknowledgements

YourKit is kindly supporting open source projects with its full-featured Java Profiler.
YourKit, LLC is the creator of innovative and intelligent tools for profiling
Java and .NET applications. Take a look at YourKit's leading software products:
[YourKit Java Profiler](http://www.yourkit.com/java/profiler/index.jsp) and
[YourKit .NET Profiler](http://www.yourkit.com/.net/profiler/index.jsp).

Many thanks to Jetbrains for providing us with an OSS licence for their fine development 
tools such as [IntelliJ IDEA](https://www.jetbrains.com/idea/).

Also, thanks to https://github.com/fthomas/scala-steward for helping to keep our dependencies updated automatically.
