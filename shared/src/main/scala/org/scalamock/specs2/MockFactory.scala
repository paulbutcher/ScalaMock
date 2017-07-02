// Copyright (c) 2011-2015 ScalaMock Contributors (https://github.com/paulbutcher/ScalaMock/graphs/contributors)
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

package org.scalamock.specs2

import org.scalamock.MockFactoryBase
import org.specs2.execute.{AsResult, Failure, FailureException}
import org.specs2.main.ArgumentsShortcuts
import org.specs2.specification.{Around, AroundEach}

/**
 * Base trait for MockContext and IsolatedMockFactory
 *
 * @define techniques
 * To use ScalaMock in Specs2 tests you can either:
 *  - mix `Specification` with [[org.scalamock.specs2.IsolatedMockFactory]] to
 *    get '''isolated test cases''' or
 *  - run each test case in separate '''fixture context''' - [[org.scalamock.specs2.MockContext]]
 */
trait MockContextBase extends MockFactoryBase {

  type ExpectationException = FailureException

  protected override def newExpectationException(message: String, methodName: Option[Symbol]) =
    new ExpectationException(Failure(message))

  protected def wrapAsResult[T: AsResult](body: => T) = {
    AsResult(withExpectations { body })
  }
}

/**
 * Fixture context that should be created per test-case basis
 *
 * $techniques
 *
 * '''Fixture contexts''' are more flexible and are recommened for complex test suites where single
 * set of fixtures does not fit all test cases.
 *
 * ==Basic usage==
 *
 * For simple test cases it's enough to run test case in new `MockContext` scope.
 *
 * {{{
 * class BasicCoffeeMachineTest extends Specification {
 *
 *  	"CoffeeMachine" should {
 *  	     "not turn on the heater when the water container is empty" in new MockContext {
 *  	         val waterContainerMock = mock[WaterContainer]
 *  	         (waterContainerMock.isOverfull _).expects().returning(true)
 *  	         // ...
 *  	     }
 *
 *  	     "not turn on the heater when the water container is overfull" in new MockContext {
 *  	         val waterContainerMock = mock[WaterContainer]
 *  	         // ...
 *  	     }
 *  	}
 * }
 * }}}
 *
 * ==Complex fixture contexts==
 *
 * When multiple test cases need to work with the same mocks (and more generally - the same
 * fixtures: files, sockets, database connections, etc.) you can use fixture contexts.
 *
 * {{{
 * class CoffeeMachineTest extends Specification {
 *
 * 	trait Test extends MockContext { // fixture context
 * 	    // shared objects
 * 	    val waterContainerMock = mock[WaterContainer]
 * 	    val heaterMock = mock[Heater]
 * 	    val coffeeMachine = new CoffeeMachine(waterContainerMock, heaterMock)
 *
 * 	    // test setup
 * 	    coffeeMachine.powerOn()
 * 	}
 *
 * 	// you can extend and combine fixture-contexts
 * 	trait OverfullWaterContainerTest extends Test {
 * 	    // you can set expectations and use mocks in fixture-context
 * 	    (waterContainerMock.isEmpty _).expects().returning(true)
 *
 * 	    // and define helper functions
 * 	    def complexLogic() {
 * 	        coffeeMachine.powerOff()
 * 	        // ...
 * 	    }
 * 	}
 *
 * 	"CoffeeMachine" should {
 * 	     "not turn on the heater when the water container is empty" in new MockContext {
 * 	         val heaterMock = mock[Heater]
 * 	         val waterContainerMock = mock[WaterContainer]
 * 	         val coffeeMachine = new CoffeeMachine(waterContainerMock, heaterMock)
 * 	         (waterContainerMock.isOverfull _).expects().returning(true)
 * 	         // ...
 * 	     }
 *
 * 	     "not turn on the heater when the water container is overfull" in new OverfullWaterContainerTest {
 * 	         // ...
 * 	         complexLogic()
 * 	     }
 * 	}
 * }
 * }}}
 */
trait MockContext extends MockContextBase with Around {

  override def around[T: AsResult](body: => T) = {
    wrapAsResult[T] { body }
  }
}

/**
 * A trait that can be mixed into a [[http://etorreborre.github.com/specs2/ Specs2]] specification to provide
 * mocking support.
 *
 * $techniques
 *
 * '''Isolated tests cases''' are clean and simple, recommended when all test cases have the same
 *    or very similar fixtures
 *
 * {{{
 * class IsolatedCoffeeMachineTest extends Specification with IsolatedMockFactory {
 * 	
 * 	// shared objects
 * 	val waterContainerMock = mock[WaterContainer]
 * 	val heaterMock = mock[Heater]
 * 	val coffeeMachine = new CoffeeMachine(waterContainerMock, heaterMock)
 *
 * 	// you can set common expectations in suite scope
 * 	(waterContainerMock.isOverfull _).expects().returning(true)
 *
 * 	// test setup
 * 	coffeeMachine.powerOn()
 *
 * 	"CoffeeMachine" should {
 * 	    "not turn on the heater when the water container is empty" in {
 * 	        coffeeMachine.isOn must_== true
 * 	        // ...
 * 	        coffeeMachine.powerOff()
 * 	        coffeeMachine.isOn must_== false
 * 	    }
 *
 * 	    "not turn on the heater when the water container is overfull" in {
 * 	        // each test case uses separate, fresh Suite so the coffee machine is turned on
 * 	        coffeeMachine.isOn must_== true
 * 	        // ...
 * 	    }
 * 	}
 * }
 * }}}
 */
trait IsolatedMockFactory extends AroundEach with MockContextBase { self: ArgumentsShortcuts =>
  isolated

  override def around[T: AsResult](body: => T) = {
    wrapAsResult[T] { body }
  }
}

/**
 * A trait that can be mixed into a [[http://etorreborre.github.com/specs2/ Specs2]] specification to provide
 * mocking support.
 *
 * MockFactory does not work well in with thread pools and futures. Fixture-contexts support is also broken.
 *
 * $techniques
 */
@deprecated("MockFactory is buggy. Please use IsolatedMockFactory or MockContext instead", "3.2")
trait MockFactory extends AroundEach with MockContextBase { self: ArgumentsShortcuts =>

  override def around[T: AsResult](body: => T) = {
    wrapAsResult[T] { body }
  }
}
