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
  import language.implicitConversions
  
  def mock[T](implicit factory: MockFactoryBase) = macro MockImpl.mock[T]
  
  implicit def toMockFunction0[R](f: Function0[R]) = macro MockImpl.toMockFunction0[R]
  implicit def toMockFunction1[T1,  R](f: Function1[T1, R]) = macro MockImpl.toMockFunction1[T1, R]
  implicit def toMockFunction2[T1, T2, R](f: Function2[T1, T2, R]) = macro MockImpl.toMockFunction2[T1, T2, R]
}

object MockImpl {
  
  def mock[T: c.TypeTag](c: Context)(factory: c.Expr[MockFactoryBase]): c.Expr[T] = {
    import c.mirror._
    import reflect.api.Modifier._
    
    val anon = newTypeName("$anon") 

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
                   Select(Select(This(anon), mockFunctionName(m, t)), newTermName("apply")),
                   paramss(mt).flatten map { p => Ident(newTermName(p.name.toString)) })
      methodImpl(m, mt, body)
    }
    
    def mockFunctionClass(paramCount: Int): TypeTag[_] = paramCount match {
      case 0 => implicitly[TypeTag[MockFunction0[_]]]
      case 1 => implicitly[TypeTag[MockFunction1[_, _]]]
      case 2 => implicitly[TypeTag[MockFunction2[_, _, _]]]
      case _ => sys.error("Can't handle methods with more than 2 parameters (yet)")
    }
    
    def mockFunctionName(m: Symbol, t: Type) = {
      val method = t.member(m.name)
      newTermName("mock$"+ m.name +"$"+ method.alternatives.indexOf(m))
    }
    
    def mockParamType(t: Type) = {
      val sym = t.typeSymbol
      if (sym.modifiers contains parameter)
        staticClass("scala.Any")
      else
        sym
    }  
    
    def mockFunction(m: Symbol, t: Type) = {
      val clazz = mockFunctionClass(paramCount(t))
      val types = (paramTypes(t) map { pt => Ident(mockParamType(pt)) }) :+ Ident(mockParamType(finalResultType(t)))
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
        mockFunctionName(m, t), 
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
      
    def isMemberOfObject(m: Symbol) = TypeTag.Object.tpe.member(m.name) != NoSymbol

    // new { <|members|> }
    def anonClass(parents: List[TypeTree], members: List[Tree]) =
      Block(
        List(
          ClassDef(
            Modifiers(Set(`final`)), 
            anon,
            List(),
            Template(
              parents, 
              emptyValDef,
              initDef +: members))),
        Apply(
          Select(
            New(Ident(anon)), 
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
    val members = forwarders ++ mocks
    
    val result = castTo(anonClass(List(TypeTree(tpe)), members), tpe)
    
//    println("------------")
//    println(showRaw(result))
//    println("------------")
//    println(show(result))
//    println("------------")

    c.Expr(result)
  }
  
  // Given something of the structure <|o.m _|> where o is a mock object
  // and m is a method, find the corresponding MockFunction instance
  def findMockFunction[F: c.TypeTag, M: c.TypeTag](c: Context)(f: c.Expr[F], actuals: List[c.mirror.Type]): c.Expr[M] = {
    import c.mirror._
    
    def mockFunctionName(name: Name, t: Type) = {
      val method = t.member(name)
      if (method.isOverloaded) {
        val m = method.resolveOverloaded(NoPrefix, List(), actuals)
        "mock$"+ name +"$"+ method.alternatives.indexOf(m)
      } else {
        "mock$"+ name +"$0"
      }    
    }

    val (obj, name) = f.tree match {
      case Block(_, Function(_, Apply(Select(o, n), _))) => (o, n)
      case Function(_, Select(o, n)) => (o, n)
      case Function(_, Apply(Select(o, n), _)) => (o, n)
      case Function(_, Apply(Apply(Select(o, n), _), _)) => (o, n)
      case Function(_, Apply(Apply(Apply(Select(o, n), _), _), _)) => (o, n)
      case Function(_, Apply(Apply(Apply(Apply(Select(o, n), _), _), _), _)) => (o, n)
      case Function(_, Apply(TypeApply(Select(o, n), _), _)) => (o, n)
      case Function(_, Apply(Apply(TypeApply(Select(o, n), _), _), _)) => (o, n)
      case Function(_, Apply(Apply(Apply(TypeApply(Select(o, n), _), _), _), _)) => (o, n)
      case Function(_, Apply(Apply(Apply(Apply(TypeApply(Select(o, n), _), _), _), _), _)) => (o, n)
      case _ => sys.error("Unrecognised structure: "+ showRaw(f.tree))
    }
    c.Expr(
      TypeApply(
        Select(
          Apply(
            Select(
              Apply(
                Select(
                  Apply(Select(obj, newTermName("getClass")), List()),
                  newTermName("getMethod")),
                List(Literal(Constant(mockFunctionName(name, obj.tpe))))),
              newTermName("invoke")),
            List(obj)),
          newTermName("asInstanceOf")),
        List(TypeTree(c.tag[M].tpe))))
  }

  def toMockFunction0[R: c.TypeTag](c: Context)(f: c.Expr[Function0[R]]) =
    findMockFunction[Function0[R], MockFunction0[R]](c)(f, List())

  def toMockFunction1[T1: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[Function1[T1, R]]) =
    findMockFunction[Function1[T1, R], MockFunction1[T1, R]](c)(f, List(c.tag[T1].tpe))

  def toMockFunction2[T1: c.TypeTag, T2: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[Function2[T1, T2, R]]) =
    findMockFunction[Function2[T1, T2, R], MockFunction2[T1, T2, R]](c)(f, List(c.tag[T1].tpe, c.tag[T2].tpe))
}