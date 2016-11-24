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

package org.scalamock.clazz

import org.scalamock.util.MacroUtils

object MockFunctionFinder {

  import scala.reflect.macros.blackbox.Context

  /**
   * Given something of the structure <|o.m _|> where o is a mock object
   * and m is a method, find the corresponding MockFunction instance
   */
  def findMockFunction[F: c.WeakTypeTag, M: c.WeakTypeTag](c: Context)(f: c.Expr[F], actuals: List[c.universe.Type]): c.Expr[M] = {
    import c.universe._

    val utils = new MacroUtils[c.type](c)
    import utils._

    def reportError(message: String) = {
      // Report with both info and abort so that the user still sees something, even if this is within an
      // implicit conversion (see https://issues.scala-lang.org/browse/SI-5902)
      c.info(c.enclosingPosition, message, true)
      c.abort(c.enclosingPosition, message)
    }

    def sameTypes(types1: List[Type], types2: List[Type]) = {
      // see issue #34
      var these = types1.map(_.dealias)
      var those = types2.map(_.dealias)
      while (!these.isEmpty && !those.isEmpty && these.head =:= those.head) {
        these = these.tail
        those = those.tail
      }
      these.isEmpty && those.isEmpty
    }

    // This performs a ridiculously simple-minded overload resolution, but it works well enough for
    // our purposes, and is much easier than trying to backport the implementation that was deleted
    // from the macro API (c.f. https://groups.google.com/d/msg/scala-internals/R1iZXfotqds/3xytfX39U2wJ)
    //! TODO - replace with official resolveOverloaded if/when it's reinstated
    def resolveOverloaded(method: TermSymbol, targs: List[Type]): Symbol = {
      method.alternatives find { m =>
        val tpe = m.typeSignature
        val pts = {
          if (targs.nonEmpty && tpe.typeParams.length == targs.length)
            paramTypes(appliedType(tpe, targs))
          else
            paramTypes(tpe)
        }
        sameTypes(pts, actuals)
      } getOrElse {
        reportError(s"Unable to resolve overloaded method ${method.name}")
      }
    }

    def mockFunctionName(name: Name, t: Type, targs: List[Type]) = {
      val method = t.member(name).asTerm
      if (method.isOverloaded)
        "mock$" + name + "$" + method.alternatives.indexOf(resolveOverloaded(method, targs))
      else
        "mock$" + name + "$0"
    }

    // mock.getClass().getMethod(name).invoke(obj).asInstanceOf[MockFunctionX[...]]
    def mockedFunctionGetter(obj: Tree, name: Name, targs: List[Type]): c.Expr[M] = {
      val method = applyOn(applyOn(obj, "getClass"), "getMethod", literal(mockFunctionName(name, obj.tpe, targs)))
      c.Expr(castTo(applyOn(method, "invoke", obj), weakTypeOf[M]))
    }

    def transcribeTree(tree: Tree, targs: List[Type] = Nil): c.Expr[M] = {
      tree match {
        case Select(qualifier, name) => mockedFunctionGetter(qualifier, name, targs)
        case Block(stats, expr) => c.Expr[M](Block(stats, transcribeTree(expr).tree)) // see issue #62
        case Typed(expr, tpt) => transcribeTree(expr)
        case Function(vparams, body) => transcribeTree(body)
        case Apply(fun, args) => transcribeTree(fun)
        case TypeApply(fun, args) => transcribeTree(fun, args.map(_.tpe));
        case _ => reportError(
          s"ScalaMock: Unrecognised structure: ${showRaw(tree)}." +
            "Please open a ticket at https://github.com/paulbutcher/ScalaMock/issues")
      }
    }

    transcribeTree(f.tree)
  }

}
