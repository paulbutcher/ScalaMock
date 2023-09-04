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

import org.scalamock.util.MacroAdapter

object MockFunctionFinderImpl {
  import MacroAdapter.Context

  def mockedFunctionGetter[M: c.WeakTypeTag](c: Context)
                                            (obj: c.Tree, name: c.Name, targs: List[c.Type], actuals: List[c.universe.Type]): c.Expr[M] = {
    import c.universe._

    def mockFunctionName(name: Name, t: Type, targs: List[Type]) = {
      val method = t.member(name).asTerm
      if (method.isOverloaded)
        "mock$" + name + "$" + method.alternatives.indexOf(MockFunctionFinder.resolveOverloaded(c)(method, targs, actuals))
      else
        "mock$" + name + "$0"
    }

    val code = c.Expr[M](q"""
        $obj.getClass().getMethod(${mockFunctionName(name, obj.tpe, targs)}).invoke($obj).asInstanceOf[${weakTypeOf[M]}]
       """)
    code
  }
}
