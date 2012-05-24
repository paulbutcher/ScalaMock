// Copyright (c) 2011-2012 Paul Butcher
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

package org.scalamock

import reflect.makro.Context

trait Mock {
  import language.experimental.macros
  
  def mock[T] = macro MockImpl.mock[T]
}

object MockImpl {
  
  def mock[T: c.TypeTag](c: Context): c.Expr[T] = {
    import c.mirror._
    import reflect.api.Modifier._

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
    
    def buildParams(methodType: Type) =
      paramss(methodType) map { params =>
        params map { p =>
          val paramType = p.asTypeIn(methodType).typeSymbol
          val paramTypeTree = 
            if (paramType.modifiers contains parameter)
              Ident(newTypeName(paramType.name.toString))
            else
              Ident(paramType)
              
          ValDef(
            Modifiers(Set(parameter)),
            newTermName(p.name.toString),
            paramTypeTree,
            EmptyTree)
        }
      }
    
    def buildTypeParams(methodType: Type) =
      methodType.typeParams map { t => 
        TypeDef(
          Modifiers(Set(parameter)),
          newTypeName(t.name.toString), 
          List(), 
          TypeBoundsTree(Ident(staticClass("scala.Nothing")), Ident(staticClass("scala.Any"))))
      }
    
    // def <|name|>(p1: T1, p2: T2, ...): T = null.asInstanceOf[T]
    def methodDef(name: Name, methodType: Type): DefDef = {
      val params = buildParams(methodType)
      val body = TypeApply(
                   Select(Literal(Constant(null)), newTermName("asInstanceOf")), 
                   List(TypeTree().setType(finalResultType(methodType)))) 
      DefDef(
        Modifiers(),
        name, 
        buildTypeParams(methodType), 
        params,
        TypeTree(),
        body)
    }
    
    def methodImpl(m: Symbol, t: Type): DefDef = {
      val mt = m.asTypeIn(t) 
      mt match {
        case NullaryMethodType(_) => methodDef(m.name, mt)
        case MethodType(_, _) => methodDef(m.name, mt)
        case PolyType(_, _) => methodDef(m.name, mt)
        case _ => sys.error("Don't know how to handle "+ mt.getClass)
      }
    }
    
    def mockFunctionClass(paramCount: Int): TypeTag[_] = paramCount match {
      case 0 => implicitly[TypeTag[MockFunction0[_]]]
      case 1 => implicitly[TypeTag[MockFunction1[_, _]]]
      case 2 => implicitly[TypeTag[MockFunction2[_, _, _]]]
      case _ => sys.error("Can't handle methods with more than 2 parameters (yet)")
    }
    
    def mockFunction(m: Symbol, t: Type) = {
      val clazz = mockFunctionClass(paramCount(t)) 
      Apply(
        Select(
          New(
            AppliedTypeTree(
              Ident(clazz.sym),
              List(Ident(newTypeName("String")), Ident(newTypeName("String"))))), 
          newTermName("<init>")),
        List(
          Literal(Constant(null)), 
          Apply(
            Select(Select(Ident(newTermName("scala")), newTermName("Symbol")), newTermName("apply")),
            List(Literal(Constant(m.name.toString))))))
    }
    
    def mockMethod(m: Symbol, t: Type): ValDef = {
      val mt = m.asTypeIn(t)
      ValDef(Modifiers(),
        newTermName("mock$"+ m.name.toString), 
        TypeTree(), 
        mockFunction(m, mt))
    }
    
    // def <init>() = { super.<init>(); () }
    def initDef = 
      DefDef(
        Modifiers(), 
        newTermName("<init>"), 
        List(), 
        List(List()), 
        TypeTree(),
        Block(
          List(
            Apply(
              Select(Super(This(newTypeName("")), newTypeName("")), newTermName("<init>")), 
              List())), 
          Literal(Constant(()))))
      
    def isMemberOfObject(m: Symbol) = TypeTag.Object.tpe.member(m.name) != NoSymbol

    // { final class $anon extends T { ... }; new $anon() }.asInstanceOf[T])
    def anonClass(t: Type) = {
      val methodsToMock = t.members filterNot (m => isMemberOfObject(m))
      val forwarders = (methodsToMock map (m => methodImpl(m, t))).toList
      val mocks = (methodsToMock map (m => mockMethod(m, t))).toList
      val ttree = TypeTree().setType(t)
      TypeApply(
        Select(
          Block(
            List(
              ClassDef(
                Modifiers(Set(`final`)), 
                newTypeName("$anon"),
                List(),
                Template(
                  List(ttree), 
                  emptyValDef,
                  initDef +: (forwarders ++ mocks)))),
            Apply(
              Select(
                New(Ident(newTypeName("$anon"))), 
                newTermName("<init>")), 
              List())),
          newTermName("asInstanceOf")),
        List(ttree))
    }

    c.Expr(anonClass(c.tag[T].tpe))
  }
}