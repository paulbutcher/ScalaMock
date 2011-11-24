# ScalaMock

Native Scala mocking.

## Examples

### Mocking objects

    def testTurtle {
      val t = mock[Turtle]

      t.expects.penDown()
      t.expects.turn(90.0)
      t.expects.forward(10.0)
      t.expects.getPosition returning (0.0, 10.0)
  
      drawLine(t)
    }

### Mocking functions

    def testFoldLeft() {
      val f = mockFunction[String, Int, String]

      f expects ("initial", 0) returning "intermediate one"
      f expects ("intermediate one", 1) returning "intermediate two"
      f expects ("intermediate two", 2) returning "intermediate three"
      f expects ("intermediate three", 3) returning "final"

      expect("final") { Seq(0, 1, 2, 3).foldLeft("initial")(f) }
    }

[Full worked example](http://www.paulbutcher.com/2011/10/scalamock-step-by-step/).

## Features

As well as traits (interfaces) and functions, ScalaMock can also mock:

* Classes
* Singleton and companion objects (static methods)
* Object creation (constructor invocation)
* Polymorphic (type parameterised) methods
* Classes with private constructors
* Final classes and classes with final methods
* Operators (methods with symbolic names)
* Overloaded methods

Known limitations:

* No support (yet) for type-parameterised classes.
* No support (yet) for static methods defined in Java.
* No support (yet) for methods that take by-name parameters.
* Methods that take a single tuple argument are broken on 2.9.1 due to a [compiler bug](https://issues.scala-lang.org/browse/SI-5067).

## Downloading

Download from [scala-tools](http://scala-tools.org/repo-releases/org/scalamock/).

To use ScalaMock in [sbt 0.11](https://github.com/harrah/xsbt/wiki) with [ScalaTest](http://www.scalatest.org/) add the following to your project file:

    libraryDependencies +=
      "org.scalamock" %% "scalamock-scalatest-support" % "latest.integration"

## Documentation

[Scaladoc](http://scalamock.org/api/index.html#org.scalamock.package).

## Building

Install [sbt 0.11](https://github.com/harrah/xsbt/wiki) and then:

    sbt generate-mocks
    sbt test
