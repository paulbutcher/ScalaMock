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
* built for Scala 2.11, 2.12, 2.13
* Scala 2.10 support up to ScalaMock 4.2.0

## Using ScalaMock

Artefacts are published to Maven Central and Sonatype OSS.

For ScalaTest, to use ScalaMock in your Tests, add the following to your `build.sbt`:

```scala
libraryDependencies += Seq("org.scalamock" %% "scalamock" % "4.4.0" % Test,
    "org.scalatest" %% "scalatest" % "3.2.0" % Test)
```

## Documentation

For usage in Maven or Gradle, integration with Specs2, and more example examples see the [User Guide](http://scalamock.org/user-guide/)

## Donations

This project is run for fun as a hobby, but if you want to show your appreciation you can donate some ETH or tokens to 0x6A34A63dbD851e4BEC2D25eC7d4E7b50e213C3A7 ([QR Code](https://chart.googleapis.com/chart?chs=300x300&cht=qr&chl=0x6A34A63dbD851e4BEC2D25eC7d4E7b50e213C3A7&choe=UTF-8)). Or other crypto currencies (Bitcoin, etc) via Changelly [here](https://changelly.com/). If you want to learn more about Ethereum, visit the [homepage](https://www.ethereum.org/) or [wikipedia](https://en.wikipedia.org/wiki/Ethereum).

## Acknowledgements

YourKit is kindly supporting open source projects with its full-featured Java Profiler.
YourKit, LLC is the creator of innovative and intelligent tools for profiling
Java and .NET applications. Take a look at YourKit's leading software products:
[YourKit Java Profiler](http://www.yourkit.com/java/profiler/index.jsp) and
[YourKit .NET Profiler](http://www.yourkit.com/.net/profiler/index.jsp).

Many thanks to Jetbrains for providing us with an OSS licence for their fine development 
tools such as [IntelliJ IDEA](https://www.jetbrains.com/idea/).

Also, thanks to https://github.com/fthomas/scala-steward we are keeping dependencies updated automatically, and to https://travis-ci.org/ running CI/CD automatically for every commit and PR.
