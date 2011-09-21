# Borachio

Native Scala mocking.

## Examples

### Mocking objects

    def testTurtle {
      val t = mock[Turtle]

      t expects 'penDown
      t expects 'turn withArgs (90.0)
      t expects 'forward withArgs (10.0)
      t expects 'getPosition returning (0.0, 10.0)
  
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

[Full worked example](http://www.paulbutcher.com/2011/02/mocking-in-scala-with-borachio-step-by-step/).

For more examples, see `core_tests/src/test/scala/examples`.

[Using Borachio on Android](http://www.paulbutcher.com/2011/03/mock-objects-on-android-with-borachio-part-1/).

[Android, Dependency Injection and Mock Testing](https://github.com/jaley/borachio-warehouse)

## Installation

To use Borachio in [sbt 0.10](https://github.com/harrah/xsbt/wiki) add the following to your project file:

    libraryDependencies ++= Seq("com.borachio" %% "borachio-core" % "latest.integration",
        "com.borachio" %% "borachio-scalatest-support" % "latest.integration")
    
[Maven](http://maven.apache.org/) users, or if you just want to download the JARs, [look here](http://scala-tools.org/repo-releases/com/borachio/).

## Building

Install [sbt 0.10.1](https://github.com/harrah/xsbt/wiki) and then:

    sbt test

## Design Principles

Existing Java mocking libraries work with Scala but:

* They don't handle things that Java doesn't support, such as curried functions, higher-order functions, etc.
* None of the Java mocking libraries support Android (because the Dalvik VM doesn't support runtime code generation). Borachio does not depend on [cglib](http://cglib.sourceforge.net/), meaning that it runs just fine on Android.

## To do

* Better handling of overloaded methods
* Better handling of primitive types (when returning them from a proxy mock)

## License

[MIT License](http://www.opensource.org/licenses/mit-license.php). See LICENSE.

## Name

Borachio is a character from _Much Ado About Nothing_, the plot of which revolves around identity and impersonation. Thanks to [J. Nathan Matias](http://www.natematias.com/) for the suggestion.

## Thanks

* The inner workings of Borachio's proxy mocks were inspired by [`scala.tools.reflect.Mock`](http://lampsvn.epfl.ch/svn-repos/scala/scala/trunk/src/compiler/scala/tools/reflect/Mock.scala) written by Paul Philips in the Scala compiler.

* Bill Venners for discussion and suggestions.

* [Daniel Westheide](https://github.com/dwestheide) for [specs2](http://etorreborre.github.com/specs2/) integration.