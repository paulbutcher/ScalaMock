package com.borachio

import org.scalatest.WordSpec

trait Trait1 { def method1(x: Int, y: String): String }
trait Trait2
trait Trait3

abstract class Class1 extends Trait1 with Trait2

class ProxyTest extends WordSpec {
  
  def unimplemented(proxy: AnyRef, name: Symbol, args: Array[AnyRef]): AnyRef = error("unimplemented")
  
  "A proxy" should {
    
    "implement a single interface" in {
      val p = Proxy.create(classOf[Trait1]) { unimplemented _ }
      assert(p.isInstanceOf[Trait1])
    }
    
    "implement multiple interfaces" in {
      val p = Proxy.create(classOf[Trait1], classOf[Trait2], classOf[Trait3]) { unimplemented _ }
      assert(p.isInstanceOf[Trait1])
      assert(p.isInstanceOf[Trait2])
      assert(p.isInstanceOf[Trait3])
    }
    
    "implement all the interfaces of a class" in {
      val p = Proxy.create(classOf[Class1]) { unimplemented _ }
      assert(p.isInstanceOf[Trait1])
      assert(p.isInstanceOf[Trait2])
    }
    
    "implement a mixture of classes and interfaces" in {
      val p = Proxy.create(classOf[Class1], classOf[Trait3]) { unimplemented _ }
      assert(p.isInstanceOf[Trait1])
      assert(p.isInstanceOf[Trait2])
      assert(p.isInstanceOf[Trait3])
    }
    
    "forward calls" in {
      val p = Proxy.create(classOf[Trait1]) { (proxy: AnyRef, name: Symbol, args: Array[AnyRef]) =>
        expect(name) { 'method1 }
        expect(2) { args.length }
        expect(42) { args(0).asInstanceOf[Int] }
        expect("foo") { args(1).asInstanceOf[String] }
        "called"
      }.asInstanceOf[Trait1]

      expect("called") { p.method1(42, "foo") }
    }
  }
}
