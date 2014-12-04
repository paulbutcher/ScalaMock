package org.scalamock.util

import scala.reflect.macros.blackbox.Context

/**
 * Helper functions to work with Scala macros and to create scala.reflect Trees.
 */
private[scalamock] class MacroUtils[C <: Context](val ctx2: C) { // ctx2 to avoid clash with ctx in MockMaker (eugh!)
  import ctx2.universe._

  // Convert a methodType into its ultimate result type
  // For nullary and normal methods, this is just the result type
  // For curried methods, this is the final result type of the result type
  def finalResultType(methodType: Type): Type = methodType match {
    case NullaryMethodType(result) => result
    case MethodType(_, result) => finalResultType(result)
    case PolyType(_, result) => finalResultType(result)
    case _ => methodType
  }

  // Convert a methodType into a list of lists of params:
  // UnaryMethodType => Nil
  // Normal method => List(List(p1, p2, ...))
  // Curried method => List(List(p1, p2, ...), List(q1, q2, ...), ...)
  def paramss(methodType: Type): List[List[Symbol]] = methodType match {
    case MethodType(params, result) => params :: paramss(result)
    case PolyType(_, result) => paramss(result)
    case _ => Nil
  }

  def paramCount(methodType: Type): Int = methodType match {
    case MethodType(params, result) => params.length + paramCount(result)
    case PolyType(_, result) => paramCount(result)
    case _ => 0
  }

  def paramTypes(methodType: Type): List[Type] = paramss(methodType).flatten map { _.typeSignature }

  def isMemberOfObject(m: Symbol) = TypeTag.Object.tpe.member(m.name) != NoSymbol

  // <|expr|>.asInstanceOf[<|t|>]
  def castTo(expr: Tree, t: Type): Tree = TypeApply(selectTerm(expr, "asInstanceOf"), List(TypeTree(t)))

  val scalaPredef: Tree = selectTerm(Ident(TermName("scala")), "Predef")
  val scalaSymbol: Tree = selectTerm(Ident(TermName("scala")), "Symbol")
  val scalaString: Tree = Select(scalaPredef, TypeName("String"))

  def literal(str: String): Literal = Literal(Constant(str))
  def selectTerm(qualifier: Tree, name: String): Tree = Select(qualifier, TermName(name))
  def applyListOn(qualifier: Tree, name: String, args: List[Tree]): Tree = Apply(selectTerm(qualifier, name), args)
  def applyOn(qualifier: Tree, name: String, args: Tree*): Tree = applyListOn(qualifier, name, args.toList)
  def callConstructor(obj: Tree, args: Tree*): Tree = Apply(selectTerm(obj, "<init>"), args.toList)
}
