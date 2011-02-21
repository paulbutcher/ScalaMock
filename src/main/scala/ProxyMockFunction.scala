package com.borachio

private[borachio] class ProxyMockFunction(expectations: UnorderedExpectations) extends MockFunction(expectations) {

  def apply(args: Array[AnyRef]) = handle(argsToProduct(args))
  
  private def argsToProduct(args: Array[AnyRef]) = args.length match {
    case 0 => None
    case 1 => new Tuple1(args(0))
    case 2 => (args(0), args(1))
    case _ => throw new IllegalArgumentException("ProxyMockFunction can't handle this many arguments")
  }
}
