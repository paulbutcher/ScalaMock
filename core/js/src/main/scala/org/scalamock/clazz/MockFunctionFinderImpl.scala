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

import org.scalamock.util.{MacroAdapter, MacroUtils}

object MockFunctionFinderImpl {
  import MacroAdapter.Context

  // obj.asInstanceOf[js.Dynamic].{name}.asInstanceOf[MockFunctionX[...]]
  def mockedFunctionGetter[M: c.WeakTypeTag](c: Context)
                                            (obj: c.Tree, name: c.Name, targs: List[c.Type], actuals: List[c.universe.Type]): c.Expr[M] = {
    import c.universe._
    val utils = new MacroUtils[c.type](c)
    import utils._

    def hasValueTypeArgs(baseSymbol: Symbol, owner: Type): Boolean = {
      val baseType = owner.baseType(baseSymbol)
      baseType.typeArgs.nonEmpty && baseType.typeArgs.forall(_ <:< typeOf[AnyVal])
    }

    // this somehow replicates postfix logic from scala-js, there have to be a better way
    def privateSuffix(owner: Tree): String = {
      val objectSymbol = c.typeOf[Object].typeSymbol

      val tpe = owner.tpe

      val baseNonInterfaceParentCount = tpe.baseClasses.count( symbol ⇒
        symbol != objectSymbol && (symbol.isClass && !symbol.asClass.isTrait)
      )

      // this was found in experiments and should be considered as magical cosmological constant
      val haveBaseClassWithValTypeArgs = tpe.typeSymbol.owner != null && tpe.typeSymbol.owner.isPackage &&
        tpe.baseClasses.headOption.forall(hasValueTypeArgs(_, owner.tpe))

      val idx = baseNonInterfaceParentCount + (if (haveBaseClassWithValTypeArgs) 1 else 0)
      "$" + idx.toString
    }

    def encodeMemberNameInternal(s: String): String = {
      s.replace("_", "$und")
    }

//    def printTypeSymbol(t: Type): String = {
//      t.toString + "[" + t.baseClasses.map( base ⇒
//        t.baseType(base).toString
//      ).mkString(",") + "]"
//    }

    def mockFunctionName(name: Name, t: Type, targs: List[Type]) = {
      val method = t.member(name).asTerm
      val nameStr = encodeMemberNameInternal(name.toString)

      if (method.isOverloaded)
        "mock$" + nameStr + "$" + method.alternatives.indexOf(MockFunctionFinder.resolveOverloaded(c)(method, targs, actuals))
      else
        "mock$" + nameStr + "$0"
    }

    val fullName = mockFunctionName(name, obj.tpe, targs) + privateSuffix(obj)
    val fld = freshTerm("fld")

    val code = c.Expr[M](q"""{
      import scala.scalajs.js
      val $fld = js.Object.getOwnPropertyDescriptor($obj.asInstanceOf[js.Object], $fullName)
      if (js.isUndefined($fld)) {
        throw new IllegalArgumentException("Property '" + $fullName + "' is not defined in '" + $obj + "'. Available properties: " +
          js.Object.getOwnPropertyNames($obj.asInstanceOf[js.Object]).mkString(",")
        )
      }
      $fld.value.asInstanceOf[${weakTypeOf[M]}]
    }""")
    // println(code)
    code
  }
}
