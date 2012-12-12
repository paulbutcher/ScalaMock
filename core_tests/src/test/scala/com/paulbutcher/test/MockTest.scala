// Copyright (c) 2011-2012 Paul Butcher
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

package com.paulbutcher.test

import reflect.runtime.universe._
import org.scalamock.scalatest.MockFactory
import org.scalatest.FreeSpec
import org.scalamock._

class MockTest extends FreeSpec with MockFactory {
  
  autoVerify = false
  
  "Mocks should" - {
    "fail if an unexpected method call is made" in {
      withExpectations {
        val m = mock[TestTrait]
        intercept[ExpectationException] { m.oneParam(42) }
      }
    }
    
    "allow expectations to be set" in {
      withExpectations {
        val m = mock[TestTrait]
        (m.twoParams _).expects(42, 1.23).returning("a return value")
        expectResult("a return value") { m.twoParams(42, 1.23) }
      }
    }
    
    "cope with nullary methods" in {
      withExpectations {
        val m = mock[TestTrait]
        (m.nullary _).expects().returning("a return value")
        expectResult("a return value") { m.nullary }
      }
    }
    
    "cope with overloaded methods" in {
      withExpectations {
        val m = mock[TestTrait]
        (m.overloaded(_: Int)).expects(10).returning("got an integer")
        (m.overloaded(_: Int, _: Double)).expects(10, 1.23).returning("got two parameters")
        expectResult("got an integer") { m.overloaded(10) }
        expectResult("got two parameters") { m.overloaded(10, 1.23) }
      }
    }
    
    "cope with polymorphic overloaded methods" in {
      withExpectations {
        val m = mock[TestTrait]
        (m.overloaded[Double] _).expects(1.23).returning("polymorphic method called")
        expectResult("polymorphic method called") { m.overloaded(1.23) }
      }
    }

    "choose between polymorphic and non-polymorphic overloaded methods correctly" in {
      withExpectations {
        val m = mock[TestTrait]
        (m.overloaded(_: Int)).expects(42).returning("non-polymorphic called")
        (m.overloaded[Int] _).expects(42).returning("polymorphic called")
        expectResult("non-polymorphic called") { m.overloaded(42) }
        expectResult("polymorphic called") { m.overloaded[Int](42) }
      }
    }
    
    "cope with infix operators" in {
      withExpectations {
        val m1 = mock[TestTrait]
        val m2 = mock[TestTrait]
        val m3 = mock[TestTrait]
        (m1.+ _).expects(m2).returning(m3)
        expectResult(m3) { m1 + m2 }
      }
    }
    
    "cope with curried methods" in {
      withExpectations {
        val m = mock[TestTrait]
        (m.curried(_: Int)(_: Double)).expects(10, 1.23).returning("curried method called")
        val partial = m.curried(10) _
        expectResult("curried method called") { partial(1.23) }
      }
    }
    
    "cope with polymorphic methods" in {
      withExpectations {
        val m = mock[TestTrait]
        (m.polymorphic(_: List[Int])).expects(List(1, 2)).returning("called with integers")
        (m.polymorphic(_: List[String])).expects(List("foo", "bar")).returning("called with strings")
        expectResult("called with integers") { m.polymorphic(List(1, 2)) }
        expectResult("called with strings") { m.polymorphic(List("foo", "bar")) }
      }
    }
    
    "cope with curried polymorphic methods" in {
      withExpectations {
        val m = mock[TestTrait]
        (m.polycurried(_: Int)(_: String)).expects(42, "foo").returning((123, "bar"))
        val partial = m.polycurried(42)(_: String)
        expectResult((123, "bar")) { partial("foo") }
      }
    }
    
    "cope with parameters of polymorphic type" in {
      withExpectations {
        val m = mock[TestTrait]
        (m.polymorphicParam _).expects((42, 1.23)).returning("it works")
        expectResult("it works") { m.polymorphicParam((42, 1.23)) }
      }
    }

    "cope with methods with repeated parameters" in {
      withExpectations {
        val m = mock[TestTrait]
        (m.repeatedParam _).expects(42, Seq("foo", "bar"))
        m.repeatedParam(42, "foo", "bar")
      }
    }
    
    "cope with methods with by name parameters" in {
      withExpectations {
        val m = mock[TestTrait]
        (m.byNameParam _).expects(*).returning("it worked")
        expectResult("it worked") { m.byNameParam(42) }
      }
    }
    
    //! TODO - find a way to make this less ugly
    "match methods with by name parameters" in {
      withExpectations {
        val m = mock[TestTrait]
        val f: (=> Int) => Boolean = { x => x == 1 && x == 2  }
        ((m.byNameParam _): (=> Int) => String).expects(new FunctionAdapter1(f)).returning("it works")
        var y = 0
        expectResult("it works") { m.byNameParam { y += 1; y } }
      }
    }
    
    "cope with methods with implicit parameters" in {
      withExpectations {
        implicit val y: Double = 1.23
        val m = mock[TestTrait]
        (m.implicitParam(_: Int)(_: Double)).expects(42, 1.23).returning("it works")
        expectResult("it works") { m.implicitParam(42) }
      }
    }
    
    "cope with a var" in {
      withExpectations {
        val m = mock[TestTrait]
        (m.aVar_= _).expects("foo")
        (m.aVar _).expects().returning("bar")
        m.aVar = "foo"
        expectResult("bar") { m.aVar }
      }
    }
    
    //! TODO - currently doesn't work because we can't override concrete vars
    "cope with a non-abstract var" ignore {
      withExpectations {
        val m = mock[TestTrait]
        (m.concreteVar_= _).expects("foo")
        (m.concreteVar _).expects().returning("bar")
        m.concreteVar = "foo"
        expectResult("bar") { m.concreteVar }
      }
    }
    
    "cope with a val" in {
      withExpectations {
        val m = mock[TestTrait]
        expectResult(null) { m.aVal }
      }
    }
    
    "cope with a non-abstract val" in {
      withExpectations {
        val m = mock[TestTrait]
        expectResult("foo") { m.concreteVal }
      }
    }
    
    "cope with a function val" in {
      withExpectations {
        val m = mock[TestTrait]
        expectResult(null) { m.fnVal }
      }
    }
    
    "cope with non-abstract methods" in {
      withExpectations {
        val m = mock[TestTrait]
        (m.withImplementation _).expects(42).returning(1234)
        expectResult(1234) { m.withImplementation(42) }
      }
    }
    
    "mock an embeddded trait" in {
      withExpectations {
        val m = mock[TestTrait]
        val e = mock[m.Embedded]
        (m.referencesEmbedded _).expects().returning(e)
        expectResult(e) { m.referencesEmbedded }
      }
    }
    
    "handle projected types correctly" in {
      withExpectations {
        val m = mock[TestTrait]
        val e = mock[m.Embedded]
        val o = mock[m.ATrait]
        val i = mock[e.ATrait]
        (e.innerTraitProjected _).expects().returning(i)
        (e.outerTraitProjected _).expects().returning(o)
        expectResult(o) { e.outerTraitProjected }
        expectResult(i) { e.innerTraitProjected }
      }
    }
    
    "handle path-dependent types correctly" in {
      withExpectations {
        val m = mock[TestTrait]
        val e = mock[m.Embedded]
        val o = mock[m.ATrait]
        val i = mock[e.ATrait]
        (e.innerTrait _).expects().returning(i)
        (e.outerTrait _).expects().returning(o)
        expectResult(o) { e.outerTrait }
        expectResult(i) { e.innerTrait }
      }
    }
    
    "cope with upper bounds" in {
      withExpectations {
        val m = mock[TestTrait]
        (m.upperBound _).expects((42, "foo")).returning(2)
        expectResult(2) { m.upperBound((42, "foo")) }
      }
    }
    
    "cope with lower bounds" in {
      withExpectations {
        val m = mock[TestTrait]
        (m.lowerBound _).expects((1, 2), List[Product]()).returning("it works")
        expectResult("it works") { m.lowerBound((1, 2), List[Product]()) }
      }
    }
    
    "cope with context bounds" in {
      withExpectations {
        val m = mock[TestTrait]
        (m.contextBound(_: String)(_: TypeTag[String])).expects("foo", typeTag[java.lang.String]).returning("it works")
        expectResult("it works") { m.contextBound("foo") }
      }
    }
    
    "cope with view bounds" in {
      withExpectations {
        val m = mock[TestTrait]
        (m.viewBound(_: Int, _: Int)(_: Int => Ordered[Int])).expects(1, 2, *).returning(true)
        expectResult(true) { m.viewBound(1, 2) }
      }
    }
    
    "mock a polymorphic trait" in {
      withExpectations {
        val m = mock[PolymorphicTrait[String]]
        (m.method[Double] _).expects(42, "foo", 1.23).returning("a return value")
        expectResult("a return value") { m.method(42, "foo", 1.23) }
      }
    }
    
    "handle path-dependent polymorphic types correctly" in {
      withExpectations {
        val m = mock[PolymorphicTrait[String]]
        val e = mock[m.Embedded[Double]]
        val o = mock[m.ATrait[String, Double]]
        val i = mock[e.ATrait[String, Double]]
        (e.innerTrait _).expects("foo", 1.23).returning(i)
        (e.outerTrait _).expects("bar", 4.56).returning(o)
        expectResult(o) { e.outerTrait("bar", 4.56) }
        expectResult(i) { e.innerTrait("foo", 1.23) }
      }
    }
    
    "mock a Java interface" in {
      withExpectations {
        val m = mock[JavaInterface]
        (m.m _).expects(42, "foo").returning("a return value")
        expectResult("a return value") { m.m(42, "foo") }
      }
    }

    //! TODO - this is going to have to wait for macro types for a proper solution
//    "cope with Java methods with repeated parameters" in {
//      withExpectations {
//        val m = mock[JavaInterface]
//        (m.repeatedParam _).expects(42, Seq(1.23, 4.56))
//        m.repeatedParam(42, 1.23, 4.56)
//      }
//    }

    "mock a class" in {
      withExpectations {
        val m = mock[TestClass]
        (m.m _).expects(42, "foo").returning((123, "bar"))
        expectResult((123, "bar")) { m.m(42, "foo") }
      }
    }
    
    "mock a specialized class" in {
      withExpectations {
        val m1 = mock[SpecializedClass[Int]]
        (m1.identity _).expects(42).returning(43)
        expectResult(43) { m1.identity(42) }
        
        val m2 = mock[SpecializedClass[List[String]]]
        (m2.identity _).expects(List("one", "two", "three")).returning(List("four", "five", "six"))
        expectResult(List("four", "five", "six")) { m2.identity(List("one", "two", "three")) }
      }
    }
  }
  
  "Stubs should" - {
    "return null unless told otherwise" in {
      withExpectations {
        val m = stub[TestTrait]
        expectResult(null) { m.oneParam(42) }
      }
    }
    
    "return what they're told to" in {
      withExpectations {
        val m = stub[TestTrait]
        (m.twoParams _).when(42, 1.23).returns("a return value")
        expectResult("a return value") { m.twoParams(42, 1.23) }
      }
    }
    
    "verify calls" in {
      withExpectations {
        val m = stub[TestTrait]
        m.twoParams(42, 1.23)
        m.twoParams(42, 1.23)
        (m.twoParams _).verify(42, 1.23).twice
      }
    }
    
    "fail when verification fails" in {
      intercept[ExpectationException](withExpectations {
        val m = stub[TestTrait]
        m.twoParams(42, 1.00)
        (m.twoParams _).verify(42, 1.23).once
      })
    }
  }
}