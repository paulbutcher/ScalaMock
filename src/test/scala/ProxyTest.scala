// Copyright (c) 2011 Paul Butcher
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
