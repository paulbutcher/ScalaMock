package org.scalamock.clazz

import org.scalamock.MockFactoryBase
import org.scalamock.function._
import org.scalamock.util.{Defaultable, MacroUtils}

object MockImpl {
  import scala.reflect.macros.blackbox.Context

  def mock[T: c.WeakTypeTag](c: Context)(factory: c.Expr[MockFactoryBase]): c.Expr[T] = {
    val maker = MockMaker[T](c)(factory, stub = false)
    maker.make
  }

  def stub[T: c.WeakTypeTag](c: Context)(factory: c.Expr[MockFactoryBase]): c.Expr[T] = {
    val maker = MockMaker[T](c)(factory, stub = true)
    maker.make
  }

  def MockMaker[T: c.WeakTypeTag](c: Context)(factory: c.Expr[MockFactoryBase], stub: Boolean) = {
    val m = new MockMaker[c.type](c)
    new m.MockMakerInner[T](factory, stub)
  }

  // Given something of the structure <|o.m _|> where o is a mock object
  // and m is a method, find the corresponding MockFunction instance
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
        val pts = if (targs.nonEmpty) paramTypes(appliedType(tpe, targs)) else paramTypes(tpe)
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

  def toMockFunction0[R: c.WeakTypeTag](c: Context)(f: c.Expr[() => R])(evidence$1: c.Expr[Defaultable[R]]) =
    findMockFunction[() => R, MockFunction0[R]](c)(f, List())

  def toMockFunction1[T1: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[T1 => R])(evidence$2: c.Expr[Defaultable[R]]) =
    findMockFunction[T1 => R, MockFunction1[T1, R]](c)(f, List(c.weakTypeOf[T1]))

  def toMockFunction2[T1: c.WeakTypeTag, T2: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2) => R])(evidence$3: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2) => R, MockFunction2[T1, T2, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2]))

  def toMockFunction3[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3) => R])(evidence$4: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3) => R, MockFunction3[T1, T2, T3, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3]))

  def toMockFunction4[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4) => R])(evidence$5: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4) => R, MockFunction4[T1, T2, T3, T4, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4]))

  def toMockFunction5[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5) => R])(evidence$6: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5) => R, MockFunction5[T1, T2, T3, T4, T5, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5]))

  def toMockFunction6[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6) => R])(evidence$7: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6) => R, MockFunction6[T1, T2, T3, T4, T5, T6, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6]))

  def toMockFunction7[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7) => R])(evidence$8: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7) => R, MockFunction7[T1, T2, T3, T4, T5, T6, T7, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7]))

  def toMockFunction8[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8) => R])(evidence$9: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8) => R, MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8]))

  def toMockFunction9[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9) => R])(evidence$10: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9) => R, MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9]))

  def toStubFunction0[R: c.WeakTypeTag](c: Context)(f: c.Expr[() => R])(evidence$20: c.Expr[Defaultable[R]]) =
    findMockFunction[() => R, StubFunction0[R]](c)(f, List())

  def toStubFunction1[T1: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[T1 => R])(evidence$21: c.Expr[Defaultable[R]]) =
    findMockFunction[T1 => R, StubFunction1[T1, R]](c)(f, List(c.weakTypeOf[T1]))

  def toStubFunction2[T1: c.WeakTypeTag, T2: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2) => R])(evidence$22: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2) => R, StubFunction2[T1, T2, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2]))

  def toStubFunction3[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3) => R])(evidence$23: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3) => R, StubFunction3[T1, T2, T3, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3]))

  def toStubFunction4[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4) => R])(evidence$24: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4) => R, StubFunction4[T1, T2, T3, T4, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4]))

  def toStubFunction5[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5) => R])(evidence$25: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5) => R, StubFunction5[T1, T2, T3, T4, T5, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5]))

  def toStubFunction6[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6) => R])(evidence$26: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6) => R, StubFunction6[T1, T2, T3, T4, T5, T6, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6]))

  def toStubFunction7[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7) => R])(evidence$27: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7) => R, StubFunction7[T1, T2, T3, T4, T5, T6, T7, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7]))

  def toStubFunction8[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8) => R])(evidence$28: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8) => R, StubFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8]))

  def toStubFunction9[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9) => R])(evidence$29: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9) => R, StubFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9]))
}
