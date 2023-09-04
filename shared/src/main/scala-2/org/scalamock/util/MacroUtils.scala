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

package org.scalamock.util

import org.scalamock.util.MacroAdapter.Context

/**
 * Helper functions to work with Scala macros and to create scala.reflect Trees.
 */
private[scalamock] class MacroUtils[C <: Context](protected val ctx: C) extends MacroAdapter {
  import ctx.universe._

  final lazy val isScalaJs =
    ctx.compilerSettings.exists(o => o.startsWith("-Xplugin:") && o.contains("scalajs-compiler"))

  def jsExport(name: String) = q"new _root_.scala.scalajs.js.annotation.JSExport($name)"

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

  def isMemberOfObject(s: Symbol) = {
    val res = TypeTag.Object.tpe.member(s.name)
    res != NoSymbol && res.typeSignature == s.typeSignature
  }

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

  def reportError(message: String) = {
    // Report with both info and abort so that the user still sees something, even if this is within an
    // implicit conversion (see https://issues.scala-lang.org/browse/SI-5902)
    ctx.info(ctx.enclosingPosition, message, true)
    ctx.abort(ctx.enclosingPosition, message)
  }
}
