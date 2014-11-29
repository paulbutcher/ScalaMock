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
        pts.map(_.dealias) sameElements actuals.map(_.dealias) // see issue #34
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
