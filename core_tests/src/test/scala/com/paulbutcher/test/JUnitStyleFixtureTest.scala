import org.scalatest.{FunSuite, OneInstancePerTest}
import org.scalamock.scalatest.MockFactory

trait Foo {
  def foo (i: Int): Int
}

//! TODO
// class JUnitStyleFixtureTest extends FunSuite with OneInstancePerTest with MockFactory {
//   val fix = mock[Foo]

//   test("this shouldn't cause an NPE") {
//     (fix.foo _) expects (1)
//     fix.foo(1)
//   }
// }