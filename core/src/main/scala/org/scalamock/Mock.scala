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
  
  def mock[T](implicit factory: MockFactoryBase) = macro MockImpl.mock[T]
}

object MockImpl {
  
  def mock[T: c.TypeTag](c: Context)(factory: c.Expr[MockFactoryBase]): c.Expr[T] = {
    import c.mirror._
    import reflect.api.Modifier._
    
    val mockName = newTypeName("$anon") 

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
    
    def paramTypes(methodType: Type): List[Type] =
      paramss(methodType).flatten map { _.asTypeIn(methodType) }
    
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
    
    // def <|name|>(p1: T1, p2: T2, ...): T = <|mockname|>(p1, p2, ...)
    def methodDef(m: Symbol, methodType: Type, body: Tree): DefDef = {
      val params = buildParams(methodType)
      DefDef(
        Modifiers(),
        m.name, 
        buildTypeParams(methodType), 
        params,
        TypeTree(),
        body)
    }
    
    def methodImpl(m: Symbol, methodType: Type, body: Tree): DefDef = {
      methodType match {
        case NullaryMethodType(_) => methodDef(m, methodType, body)
        case MethodType(_, _) => methodDef(m, methodType, body)
        case PolyType(_, _) => methodDef(m, methodType, body)
        case _ => sys.error("Don't know how to handle "+ methodType.getClass)
      }
    }
    
    def forwarderImpl(m: Symbol, t: Type): DefDef = {
      val mt = m.asTypeIn(t) 
      val body = Apply(
                   Select(Select(This(mockName), mockFunctionName(m)), newTermName("apply")),
                   paramss(mt).flatten map { p => Ident(newTermName(p.name.toString)) })
      methodImpl(m, mt, body)
    }
    
    def mockFunctionClass(paramCount: Int): TypeTag[_] = paramCount match {
      case 0 => implicitly[TypeTag[MockFunction0[_]]]
      case 1 => implicitly[TypeTag[MockFunction1[_, _]]]
      case 2 => implicitly[TypeTag[MockFunction2[_, _, _]]]
      case _ => sys.error("Can't handle methods with more than 2 parameters (yet)")
    }
    
    def mockFunctionName(m: Symbol) = newTermName("mock$"+ m.name.toString)
    
    def mockFunction(m: Symbol, t: Type) = {
      val clazz = mockFunctionClass(paramCount(t))
      val types = (paramTypes(t) map { pt => Ident(pt.typeSymbol) }) :+ Ident(finalResultType(t).typeSymbol)
      Apply(
        Select(
          New(
            AppliedTypeTree(
              Ident(clazz.sym),
              types)),
          newTermName("<init>")),
        List(
          factory.tree, 
          Apply(
            Select(Select(Ident(newTermName("scala")), newTermName("Symbol")), newTermName("apply")),
            List(Literal(Constant(m.name.toString))))))
    }
    
    // val <|mockname|> = new MockFunctionN[T1, T2, ..., R](factory, '<|name|>)
    def mockMethod(m: Symbol, t: Type): ValDef = {
      val mt = m.asTypeIn(t)
      ValDef(Modifiers(),
        mockFunctionName(m), 
        TypeTree(), 
        mockFunction(m, mt))
    }
    
    // def <init>() = super.<init>()
    def initDef = 
      DefDef(
        Modifiers(), 
        newTermName("<init>"), 
        List(), 
        List(List()), 
        TypeTree(),
        Block(
          Apply(
            Select(Super(This(newTypeName("")), newTypeName("")), newTermName("<init>")), 
            List())))
            
    def expectForwarder(m: Symbol, t: Type): DefDef = {
      val mt = m.asTypeIn(t) 
      val body = Literal(Constant(null))
      methodImpl(m, mt, body)
    }
          
    def expects(methodsToMock: List[Symbol], t: Type) = {
      val members = methodsToMock map (m => expectForwarder(m, t))
      ValDef(
        Modifiers(), 
        newTermName("expects"), 
        TypeTree(),
        anonClass(List(TypeTree(TypeTag.Object.tpe)), members))
    }
      
    def isMemberOfObject(m: Symbol) = TypeTag.Object.tpe.member(m.name) != NoSymbol

    // new { <|members|> }
    def anonClass(parents: List[TypeTree], members: List[Tree]) =
      Block(
        List(
          ClassDef(
            Modifiers(Set(`final`)), 
            mockName,
            List(),
            Template(
              parents, 
              emptyValDef,
              initDef +: members))),
        Apply(
          Select(
            New(Ident(mockName)), 
            newTermName("<init>")), 
          List()))
    
    // <|expr|>.asInstanceOf[<|t|>]
    def castTo(expr: Tree, t: Type) =
      TypeApply(
        Select(expr, newTermName("asInstanceOf")),
        List(TypeTree(t)))

    val tpe = c.tag[T].tpe
    val methodsToMock = (tpe.members filterNot (m => isMemberOfObject(m))).toList
    val forwarders = (methodsToMock map (m => forwarderImpl(m, tpe)))
    val mocks = (methodsToMock map (m => mockMethod(m, tpe)))
    val members = expects(methodsToMock, tpe) +: (forwarders ++ mocks)
    
    val result = castTo(anonClass(List(TypeTree(tpe)), members), tpe)
    
//    println("------")
//    println(show(result))
//    println("------")
//    println(showRaw(result))
//    println("------")

    c.Expr(result)
  }
}