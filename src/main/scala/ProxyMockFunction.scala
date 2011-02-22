package com.borachio

private[borachio] class ProxyMockFunction(expectations: UnorderedExpectations) extends MockFunction(expectations) {

  def apply(args: Array[AnyRef]) = handle(argsToProduct(args))
  
  private def argsToProduct(args: Array[AnyRef]) = args.length match {
    case  0 => None
    case  1 => new Tuple1(args(0))
    case  2 => (args(0), args(1))
    case  3 => (args(0), args(1), args(2))
    case  4 => (args(0), args(1), args(2), args(3))
    case  5 => (args(0), args(1), args(2), args(3), args(4))
    case  6 => (args(0), args(1), args(2), args(3), args(4), args(5))
    case  7 => (args(0), args(1), args(2), args(3), args(4), args(5), args(6))
    case  8 => (args(0), args(1), args(2), args(3), args(4), args(5), args(6), args(7))
    case  9 => (args(0), args(1), args(2), args(3), args(4), args(5), args(6), args(7), args(8))
    case 10 => (args(0), args(1), args(2), args(3), args(4), args(5), args(6), args(7), args(8), args(9))
    case _ => throw new IllegalArgumentException("ProxyMockFunction can't handle this many arguments")
  }
}
