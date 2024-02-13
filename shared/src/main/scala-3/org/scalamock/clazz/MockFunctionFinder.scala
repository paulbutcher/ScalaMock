// Copyright (c) ScalaMock Contributors (https://github.com/paulbutcher/ScalaMock/graphs/contributors)
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

import scala.quoted.*

object MockFunctionFinder:
  def ticketMessage = "Please open a ticket at https://github.com/paulbutcher/ScalaMock/issues"
  /**
   * Given something of the structure <|o.m _|> where o is a mock object
   * and m is a method, find the corresponding MockFunction instance
   */
  @scala.annotation.experimental
  def findMockFunction[M: Type](f: Expr[Any])(using quotes: Quotes): Expr[M] =
    val utils = Utils(using quotes)
    import utils.quotes.reflect.*

    def transcribeTree(term: Term, types: List[TypeTree] = Nil): Expr[M] =
      term match
        case Select(mock, methodName) =>
          val mockTpe = mock.tpe.widenTermRefByName match
            case AndType(tpe, _) => tpe
            case tpe => tpe

          val name = utils.MockableDefinitions.find(mockTpe, methodName, TypeRepr.of[M].typeArgs.init, types.map(_.tpe))
          // looks like `selectDynamic` should work with raw names, but it doesn't
          // https://github.com/lampepfl/dotty/issues/18612
          '{
             ${mock.asExpr}
               .asInstanceOf[scala.reflect.Selectable]
               .selectDynamic(${Expr(scala.reflect.NameTransformer.encode(name))})
               .asInstanceOf[M]
          }

        case Inlined(_, _, term) => transcribeTree(term)
        case Block(stats @ List(_: ValDef), term) => Block(stats, transcribeTree(term).asTerm).asExprOf[M] // var m = mock[T]
        case Block(List(DefDef(_, _, _, Some(term))), _) => transcribeTree(term)
        case Typed(term, teps) => transcribeTree(term)
        case Lambda(_, term) => transcribeTree(term)
        case Apply(term, _) => transcribeTree(term)
        case TypeApply(term, types) => transcribeTree(term, types)
        case Ident(fun) =>
          report.errorAndAbort(
            s"please declare '$fun' as MockFunctionX or StubFunctionX (e.g val $fun: MockFunction1[X, R] = ... if it has 1 parameter)"
          )
        case _ =>
          report.errorAndAbort(
            s"ScalaMock: unrecognised structure ${term.show(using Printer.TreeStructure)}. " + ticketMessage
          )
    transcribeTree(f.asTerm)
