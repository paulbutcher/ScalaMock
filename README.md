# ScalaMock 

Native Scala mocking.

Official website: [scalamock.org](https://scalamock.org/)

## Examples

### Expectations-First Style

```scala
test("drawline interaction with turtle") {
  // Create mock Turtle object
  val m = mock[Turtle]
  
  // Set expectations
  (m.setPosition _).expects(10.0, 10.0)
  (m.forward _).expects(5.0)
  (m.getPosition _).expects().returning(15.0, 10.0)

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
  (m.getPosition _).when().returns(15.0, 10.0)

  // Exercise System Under Test
  drawLine(m, (10.0, 10.0), (15.0, 10.0))

  // Verify expectations met
  (m.setPosition _).verify(10.0, 10.0)
  (m.forward _).verify(5.0)
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

## Using ScalaMock

Artefacts are published to Maven Central and Sonatype OSS.

For ScalaTest, to use ScalaMock in your Tests, add the following to your `build.sbt`:

```scala
libraryDependencies += Seq("org.scalamock" %% "scalamock" % "5.2.0" % Test,
    "org.scalatest" %% "scalatest" % "3.2.0" % Test)
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

3.
* Mockito makes use of Symbol.newClass which is marked as experimental; to avoid having to add the `@experimental`
  attribute everywhere in tests, you can add the `Test / scalacOptions += "-experimental"` to your build.


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
