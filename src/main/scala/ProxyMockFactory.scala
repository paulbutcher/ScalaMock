package com.borachio

import scala.collection.mutable.ListMap

trait ProxyMockFactory { self: MockFactory =>
  
  protected def mock[T: ClassManifest] = {
    val proxy = Proxy.create(classOf[Mock], classManifest[T].erasure) {
      (proxy: AnyRef, name: Symbol, args: Array[AnyRef]) =>
        name match {
          case 'expects => self.MockFunctionToExpectation(getOrCreate(proxy, args(0).asInstanceOf[Symbol]))
          case _ => proxies(proxy)(name)(args).asInstanceOf[AnyRef]
        }
    }
    proxies += (proxy -> ListMap[Symbol, ProxyMockFunction]())
    proxy.asInstanceOf[T with Mock]
  }
  
  private def getOrCreate(proxy: AnyRef, name: Symbol) = {
    val methods = proxies(proxy)
    if (methods contains name) {
      methods(name)
    } else {
      val m = new ProxyMockFunction(self.expectations)
      methods += name -> m
      m
    }
  }
  
  private val proxies = ListMap[AnyRef, ListMap[Symbol, ProxyMockFunction]]()
}
