# ScalaMock

Native Scala mocking.

This is ScalaMock 3, which supports Scala 2.10 only. For earlier versions of Scala, see [ScalaMock 2](https://github.com/paulbutcher/ScalaMock/tree/ScalaMock2).

## Examples

### Expectations-First Style

    def testTurtle {
      val m = mock[Turtle]                              // Create mock Turtle object
    
      (m.setPosition _).expects(10.0, 10.0)             //
      (m.forward _).expects(5.0)                        // Set expectations
      (m.getPosition _).expects().returning(15.0, 10.0) // 
    
      drawLine(m, (10.0, 10.0), (15.0, 10.0))           // Exercise System Under Test
    }

### Record-then-Verify (Mockito) Style

    def testTurtle {
      val m = stub[Turtle]                              // Create stub Turtle
      
      (m.getPosition _).when().returns(15.0, 10.0)      // Setup return values
    
      drawLine(m, (10.0, 10.0), (15.0, 10.0))           // Exercise System Under Test
    
      (m.setPosition _).verify(10.0, 10.0)              // Verify expectations met
      (m.forward _).verify(5.0)                         //
    }

[Full worked example](http://www.paulbutcher.com/2012/10/scalamock3-step-by-step/)

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

    libraryDependencies +=
      "org.scalamock" % "scalamock-scalatest-support-2.10.0-RC1" % "3.0.M5"

and with [Specs2](http://etorreborre.github.com/specs2/):

    libraryDependencies +=
      "org.scalamock" % "scalamock-specs2-support-2.10.0-RC1" % "3.0.M5"

## Documentation

[Scaladoc](http://scalamock.org/api/index.html#org.scalamock.package).

## Future Plans

When Scala supports [macro types](http://scalamacros.org/future.html), this will enable:

* 
  Improved syntax:

      mockObject.expects.method(arguments)

  instead of:

      (mockObject.method _) expects (arguments)

* Mocking object creation (constructors)
* Mocking singleton and companion objects (static methods)
* Mocking final classes and classes with final methods or private constructors
