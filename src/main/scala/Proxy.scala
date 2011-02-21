package com.borachio

import java.lang.reflect.{InvocationHandler, Method, Proxy => JavaProxy}

private[borachio] object Proxy {
  
  def interfacesFor(clazz: Class[_]): Array[Class[_]] = 
    if (clazz.isInterface)
      Array(clazz)
    else 
      clazz.getInterfaces

  def create(classes: Class[_]*)(f: (AnyRef, Symbol, Array[AnyRef]) => AnyRef) = {
    
    val interfaces = for (clazz <- classes; interface <- interfacesFor(clazz)) yield interface

    val classLoader = classes(0).getClassLoader

    val handler = new InvocationHandler {
      def invoke(proxy: AnyRef, method: Method, args: Array[AnyRef]) =
        f(proxy, Symbol(method.getName), args)
    }

    JavaProxy.newProxyInstance(classLoader, interfaces.distinct.toArray, handler)
  }
}
