# ScalaMock [![Build Status](https://travis-ci.org/paulbutcher/ScalaMock.svg?branch=master)](https://travis-ci.org/paulbutcher/ScalaMock)

Native Scala mocking.

Official website: [http://www.scalamock.org/](http://www.scalamock.org/)

## Examples

### Expectations-First Style

```scala
def testTurtle {
  val m = mock[Turtle]                              // Create mock Turtle object

  (m.setPosition _).expects(10.0, 10.0)             //
  (m.forward _).expects(5.0)                        // Set expectations
  (m.getPosition _).expects().returning(15.0, 10.0) // 

  drawLine(m, (10.0, 10.0), (15.0, 10.0))           // Exercise System Under Test
}
```

### Record-then-Verify (Mockito) Style

```scala
def testTurtle {
  val m = stub[Turtle]                              // Create stub Turtle
  
  (m.getPosition _).when().returns(15.0, 10.0)      // Setup return values

  drawLine(m, (10.0, 10.0), (15.0, 10.0))           // Exercise System Under Test

  (m.setPosition _).verify(10.0, 10.0)              // Verify expectations met
  (m.forward _).verify(5.0)                         //
}
```

[Full worked example](http://scalamock.org/quick-start/)

## Features

* Fully typesafe
* Full support for Scala features such as:
  * Polymorphic (type parameterised) methods
  * Operators (methods with symbolic names)
  * Overloaded methods
  * Type constraints
* ScalaTest and Specs2 integration

## Downloading

Download from [Sonatype](https://oss.sonatype.org/content/repositories/releases/org/scalamock/).

To use ScalaMock in [sbt](http://www.scala-sbt.org/) with [ScalaTest](http://www.scalatest.org/) add the following to your project file:

```scala
libraryDependencies +=
  "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test"
```

and with [Specs2](http://etorreborre.github.com/specs2/):

```scala
libraryDependencies +=
  "org.scalamock" %% "scalamock-specs2-support" % "3.2" % "test"
```

## Documentation

* [Quick Start](http://scalamock.org/quick-start/)
* [User Guide](http://scalamock.org/user-guide/)
* [Scaladoc](http://scalamock.org/api/index.html#org.scalamock.package)

## Future Plans

Check our [roadmap](http://scalamock.org/roadmap/).

### Acknowledgements

YourKit is kindly supporting open source projects with its full-featured Java Profiler.
YourKit, LLC is the creator of innovative and intelligent tools for profiling
Java and .NET applications. Take a look at YourKit's leading software products:
[YourKit Java Profiler](http://www.yourkit.com/java/profiler/index.jsp) and
[YourKit .NET Profiler](http://www.yourkit.com/.net/profiler/index.jsp).
