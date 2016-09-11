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

import scala.scalajs.js

object MockFunctionFinderImpl {
  import scala.reflect.macros.blackbox.Context

  // obj.asInstanceOf[js.Dynamic].{name}.asInstanceOf[MockFunctionX[...]]
  def mockedFunctionGetter[M: c.WeakTypeTag](c: Context)
                                            (obj: c.Tree, name: c.Name, targs: List[c.Type], actuals: List[c.universe.Type]): c.Expr[M] = {
    import c.universe._

    // this somehow replicates postfix logic from scala-js, there have to be a better way
    def privateSuffix(owner: Tree): String = {
      val objectSymbol = c.typeOf[Object].typeSymbol
      val idx = owner.tpe.baseClasses.count( symbol ⇒
        symbol != objectSymbol && (symbol.isClass && !symbol.asClass.isTrait)
      )
      "$" + idx.toString
    }

    def mockFunctionName(name: Name, t: Type, targs: List[Type]) = {
      val method = t.member(name).asTerm
      val nameStr = name.toString

      if (method.isOverloaded)
        "mock$" + nameStr + "$" + method.alternatives.indexOf(MockFunctionFinder.resolveOverloaded(c)(method, targs, actuals))
      else
        "mock$" + nameStr + "$0"
    }

    val utils = new MacroUtils[c.type](c)
    import utils._

    //       println(js.Object.getOwnPropertyNames($obj.asInstanceOf[js.Object]))

    val fullName = mockFunctionName(name, obj.tpe, targs) + privateSuffix(obj)
    val q = c.Expr[M](castTo(selectTerm(castTo(obj, typeOf[js.Dynamic]), fullName), weakTypeOf[M]))
    c.Expr[M](q"""{
      import scala.scalajs.js
      $q
    }""")
  }

//  private def mangleJSName(name: String) =
//    if (/*js.isKeyword(name) || */name(0).isDigit || name(0) == '$')
//      "$" + name
//    else name
}
