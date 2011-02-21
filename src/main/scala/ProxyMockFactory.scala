package com.borachio

import scala.collection.mutable.{ListBuffer, Map}

trait ProxyMockFactory { self: MockFactory =>
  
  protected def mock[T: ClassManifest] = {
    val proxy = Proxy.create(classOf[Mock], classManifest[T].erasure) {
      (proxy: AnyRef, name: Symbol, args: Array[AnyRef]) =>
        name match {
          case 'expects => self.MockFunctionToExpectation(getOrCreate(proxy, args(0).asInstanceOf[Symbol]))
          case _ => methodsFor(proxy)(name)(args).asInstanceOf[AnyRef]
        }
    }
    proxies += (proxy -> Map[Symbol, ProxyMockFunction]())
    proxy.asInstanceOf[T with Mock]
  }
  
  private def methodsFor(proxy: AnyRef) = (proxies find { _._1 eq proxy }).get._2
  
  private def getOrCreate(proxy: AnyRef, name: Symbol) = {
    val methods = methodsFor(proxy)
    if (methods contains name) {
      methods(name)
    } else {
      val m = new ProxyMockFunction(self.expectations)
      methods += name -> m
      m
    }
  }

  private val proxies = ListBuffer[(AnyRef, Map[Symbol, ProxyMockFunction])]()
}
