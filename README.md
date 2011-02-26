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

For more examples, see `src/test/scala/examples`.

## Installation

To use Borachio in [sbt](http://code.google.com/p/simple-build-tool/) add the following to your project file:

    val borachio = "com.borachio" %% "borachio" % "latest.integration"
    
[Maven](http://maven.apache.org/) users [look here](http://scala-tools.org/repo-releases/com/borachio/) to find the POM.

## Building

    sbt update
    sbt test

## Design Principles

Existing Java mocking libraries work with Scala but:

* They don't handle things that Java doesn't support, such as curried functions, higher-order functions, etc.
* None of the Java mocking libraries support Android (because the Dalvik VM doesn't support runtime code generation). Borachio does not depend on [cglib](http://cglib.sourceforge.net/), meaning that it runs just fine on Android.

## To do

* Argument matchers
* Better handling of overloaded methods
* Better handling of primitive types (when returning them from a proxy mock)

## License

[MIT License](http://www.opensource.org/licenses/mit-license.php). See LICENSE.

## Name

Borachio is a character from _Much Ado About Nothing_, the plot of which revolves around identity and impersonation. Thanks to [J. Nathan Matias](http://www.natematias.com/) for the suggestion.

## Thanks

The inner workings of Borachio's proxy mocks were inspired by [`scala.tools.reflect.Mock`](http://lampsvn.epfl.ch/svn-repos/scala/scala/trunk/src/compiler/scala/tools/reflect/Mock.scala) written by Paul Philips in the Scala compiler.

Thanks also to Bill Venners for discussion and suggestions.
