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
  
  implicit def toMockFunction0[R](f: () => R) = macro MockImpl.toMockFunction0[R]
  implicit def toMockFunction1[T1, R](f: T1 => R) = macro MockImpl.toMockFunction1[T1, R]
  implicit def toMockFunction2[T1, T2, R](f: (T1, T2) => R) = macro MockImpl.toMockFunction2[T1, T2, R]
  implicit def toMockFunction3[T1, T2, T3, R](f: (T1, T2, T3) => R) = macro MockImpl.toMockFunction3[T1, T2, T3, R]
  implicit def toMockFunction4[T1, T2, T3, T4, R](f: (T1, T2, T3, T4) => R) = macro MockImpl.toMockFunction4[T1, T2, T3, T4, R]
  implicit def toMockFunction5[T1, T2, T3, T4, T5, R](f: (T1, T2, T3, T4, T5) => R) = macro MockImpl.toMockFunction5[T1, T2, T3, T4, T5, R]
  implicit def toMockFunction6[T1, T2, T3, T4, T5, T6, R](f: (T1, T2, T3, T4, T5, T6) => R) = macro MockImpl.toMockFunction6[T1, T2, T3, T4, T5, T6, R]
  implicit def toMockFunction7[T1, T2, T3, T4, T5, T6, T7, R](f: (T1, T2, T3, T4, T5, T6, T7) => R) = macro MockImpl.toMockFunction7[T1, T2, T3, T4, T5, T6, T7, R]
  implicit def toMockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](f: (T1, T2, T3, T4, T5, T6, T7, T8) => R) = macro MockImpl.toMockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R]
  implicit def toMockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => R) = macro MockImpl.toMockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]

  implicit def toMockFunction1R[T1, R](f: (T1*) => R) = macro MockImpl.toMockFunction1R[T1, R]
  implicit def toMockFunction2R[T1, T2, R](f: (T1, T2*) => R) = macro MockImpl.toMockFunction2R[T1, T2, R]
  implicit def toMockFunction3R[T1, T2, T3, R](f: (T1, T2, T3*) => R) = macro MockImpl.toMockFunction3R[T1, T2, T3, R]
  implicit def toMockFunction4R[T1, T2, T3, T4, R](f: (T1, T2, T3, T4*) => R) = macro MockImpl.toMockFunction4R[T1, T2, T3, T4, R]
  implicit def toMockFunction5R[T1, T2, T3, T4, T5, R](f: (T1, T2, T3, T4, T5*) => R) = macro MockImpl.toMockFunction5R[T1, T2, T3, T4, T5, R]
  implicit def toMockFunction6R[T1, T2, T3, T4, T5, T6, R](f: (T1, T2, T3, T4, T5, T6*) => R) = macro MockImpl.toMockFunction6R[T1, T2, T3, T4, T5, T6, R]
  implicit def toMockFunction7R[T1, T2, T3, T4, T5, T6, T7, R](f: (T1, T2, T3, T4, T5, T6, T7*) => R) = macro MockImpl.toMockFunction7R[T1, T2, T3, T4, T5, T6, T7, R]
  implicit def toMockFunction8R[T1, T2, T3, T4, T5, T6, T7, T8, R](f: (T1, T2, T3, T4, T5, T6, T7, T8*) => R) = macro MockImpl.toMockFunction8R[T1, T2, T3, T4, T5, T6, T7, T8, R]
  implicit def toMockFunction9R[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9*) => R) = macro MockImpl.toMockFunction9R[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]

  def stub[T](implicit factory: MockFactoryBase) = macro MockImpl.stub[T]

  implicit def toStubFunction0[R](f: () => R) = macro MockImpl.toStubFunction0[R]
  implicit def toStubFunction1[T1,  R](f: T1 => R) = macro MockImpl.toStubFunction1[T1, R]
  implicit def toStubFunction2[T1, T2, R](f: (T1, T2) => R) = macro MockImpl.toStubFunction2[T1, T2, R]
  implicit def toStubFunction3[T1, T2, T3, R](f: (T1, T2, T3) => R) = macro MockImpl.toStubFunction3[T1, T2, T3, R]
  implicit def toStubFunction4[T1, T2, T3, T4, R](f: (T1, T2, T3, T4) => R) = macro MockImpl.toStubFunction4[T1, T2, T3, T4, R]
  implicit def toStubFunction5[T1, T2, T3, T4, T5, R](f: (T1, T2, T3, T4, T5) => R) = macro MockImpl.toStubFunction5[T1, T2, T3, T4, T5, R]
  implicit def toStubFunction6[T1, T2, T3, T4, T5, T6, R](f: (T1, T2, T3, T4, T5, T6) => R) = macro MockImpl.toStubFunction6[T1, T2, T3, T4, T5, T6, R]
  implicit def toStubFunction7[T1, T2, T3, T4, T5, T6, T7, R](f: (T1, T2, T3, T4, T5, T6, T7) => R) = macro MockImpl.toStubFunction7[T1, T2, T3, T4, T5, T6, T7, R]
  implicit def toStubFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](f: (T1, T2, T3, T4, T5, T6, T7, T8) => R) = macro MockImpl.toStubFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R]
  implicit def toStubFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => R) = macro MockImpl.toStubFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]

  implicit def toStubFunction1R[T1, R](f: (T1*) => R) = macro MockImpl.toStubFunction1R[T1, R]
  implicit def toStubFunction2R[T1, T2, R](f: (T1, T2*) => R) = macro MockImpl.toStubFunction2R[T1, T2, R]
  implicit def toStubFunction3R[T1, T2, T3, R](f: (T1, T2, T3*) => R) = macro MockImpl.toStubFunction3R[T1, T2, T3, R]
  implicit def toStubFunction4R[T1, T2, T3, T4, R](f: (T1, T2, T3, T4*) => R) = macro MockImpl.toStubFunction4R[T1, T2, T3, T4, R]
  implicit def toStubFunction5R[T1, T2, T3, T4, T5, R](f: (T1, T2, T3, T4, T5*) => R) = macro MockImpl.toStubFunction5R[T1, T2, T3, T4, T5, R]
  implicit def toStubFunction6R[T1, T2, T3, T4, T5, T6, R](f: (T1, T2, T3, T4, T5, T6*) => R) = macro MockImpl.toStubFunction6R[T1, T2, T3, T4, T5, T6, R]
  implicit def toStubFunction7R[T1, T2, T3, T4, T5, T6, T7, R](f: (T1, T2, T3, T4, T5, T6, T7*) => R) = macro MockImpl.toStubFunction7R[T1, T2, T3, T4, T5, T6, T7, R]
  implicit def toStubFunction8R[T1, T2, T3, T4, T5, T6, T7, T8, R](f: (T1, T2, T3, T4, T5, T6, T7, T8*) => R) = macro MockImpl.toStubFunction8R[T1, T2, T3, T4, T5, T6, T7, T8, R]
  implicit def toStubFunction9R[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9*) => R) = macro MockImpl.toStubFunction9R[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]
}

object MockImpl {
  
  def mock[T: c.TypeTag](c: Context)(factory: c.Expr[MockFactoryBase]): c.Expr[T] = {
    val maker = MockMaker(c)

    maker.make[T](factory, maker.mockFunctionClass _)
  }
  
  def stub[T: c.TypeTag](c: Context)(factory: c.Expr[MockFactoryBase]): c.Expr[T] = {
    val maker = MockMaker(c)

    maker.make[T](factory, maker.stubFunctionClass _)
  }
  
  def MockMaker(c: Context) = new MockMaker[c.type](c)
  
  class MockMaker[C <: Context](val ctx: C) {
    import ctx.mirror._
    import ctx.universe._
    import Flag._
    import definitions._
    import language.reflectiveCalls
    
    def mockFunctionClass(paramCount: Int): TypeTag[_] = paramCount match {
      case 0 => implicitly[TypeTag[MockFunction0[_]]]
      case 1 => implicitly[TypeTag[MockFunction1[_, _]]]
      case 2 => implicitly[TypeTag[MockFunction2[_, _, _]]]
      case 3 => implicitly[TypeTag[MockFunction3[_, _, _, _]]]
      case 4 => implicitly[TypeTag[MockFunction4[_, _, _, _, _]]]
      case 5 => implicitly[TypeTag[MockFunction5[_, _, _, _, _, _]]]
      case 6 => implicitly[TypeTag[MockFunction6[_, _, _, _, _, _, _]]]
      case 7 => implicitly[TypeTag[MockFunction7[_, _, _, _, _, _, _, _]]]
      case 8 => implicitly[TypeTag[MockFunction8[_, _, _, _, _, _, _, _, _]]]
      case 9 => implicitly[TypeTag[MockFunction9[_, _, _, _, _, _, _, _, _, _]]]
      case _ => ctx.abort(ctx.enclosingPosition, "ScalaMock: Can't handle methods with more than 9 parameters (yet)")
    }
    
    def stubFunctionClass(paramCount: Int): TypeTag[_] = paramCount match {
      case 0 => implicitly[TypeTag[StubFunction0[_]]]
      case 1 => implicitly[TypeTag[StubFunction1[_, _]]]
      case 2 => implicitly[TypeTag[StubFunction2[_, _, _]]]
      case 3 => implicitly[TypeTag[StubFunction3[_, _, _, _]]]
      case 4 => implicitly[TypeTag[StubFunction4[_, _, _, _, _]]]
      case 5 => implicitly[TypeTag[StubFunction5[_, _, _, _, _, _]]]
      case 6 => implicitly[TypeTag[StubFunction6[_, _, _, _, _, _, _]]]
      case 7 => implicitly[TypeTag[StubFunction7[_, _, _, _, _, _, _, _]]]
      case 8 => implicitly[TypeTag[StubFunction8[_, _, _, _, _, _, _, _, _]]]
      case 9 => implicitly[TypeTag[StubFunction9[_, _, _, _, _, _, _, _, _, _]]]
      case _ => ctx.abort(ctx.enclosingPosition, "ScalaMock: Can't handle methods with more than 9 parameters (yet)")
    }

    def make[T: ctx.TypeTag](factory: ctx.Expr[MockFactoryBase], classTag: (Int) => TypeTag[_]) = {
      val typeToMock = typeTag[T].tpe
    
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
  
      //! TODO - remove this when isStable becomes part of macro API
      def isStable(s: Symbol) = s.asInstanceOf[{ def isStable: Boolean }].isStable
      
      //! TODO - remove this when isAccessor becomes part of the macro API
      def isAccessor(s: Symbol) = s.asInstanceOf[{ def isAccessor: Boolean }].isAccessor
      
      def paramCount(methodType: Type): Int = methodType match {
        case MethodType(params, result) => params.length + paramCount(result)
        case PolyType(_, result) => paramCount(result)
        case _ => 0
      }
      
      def paramTypes(methodType: Type): List[Type] =
        paramss(methodType).flatten map { _.typeSignatureIn(methodType) }
      
      def paramType(t: Type): Tree = t match {
        case TypeRef(TypeRef(_, this_sym, _), sym, args) =>
          paramType(TypeRef(NoPrefix, sym, args))
          Ident(newTypeName(sym.name.toString))
        case TypeRef(_, sym, Nil) =>
          Ident(sym)
        case TypeRef(_, sym, args) if sym == JavaRepeatedParamClass => 
          AppliedTypeTree(Ident(RepeatedParamClass), args map mockParamType _)
        case TypeRef(_, sym, args) =>
          AppliedTypeTree(Ident(sym), args map mockParamType _)
      }
  
      def membersNotInObject = (typeToMock.members filterNot (m => isMemberOfObject(m))).toList
      
      def buildParams(methodType: Type) =
        paramss(methodType) map { params =>
          params map { p =>
            val pt = p.typeSignatureIn(methodType)
            val sym = pt.typeSymbol
            val paramTypeTree = 
              if (sym hasFlag PARAM)
                Ident(newTypeName(sym.name.toString))
              else
                paramType(pt)
                
            ValDef(
              Modifiers(PARAM),
              newTermName(p.name.toString),
              paramTypeTree,
              EmptyTree)
          }
        }
      
      def buildTypeParams(methodType: Type) =
        methodType.typeParams map { t => 
          TypeDef(
            Modifiers(PARAM),
            newTypeName(t.name.toString), 
            List(), 
            TypeBoundsTree(Ident(staticClass("scala.Nothing")), Ident(staticClass("scala.Any"))))
        }
      
      def overrideIfNecessary(m: Symbol) =
        if (nme.isConstructorName(m.name) || m.hasFlag(DEFERRED))
          Modifiers()
        else
          Modifiers(OVERRIDE)
      
      // def <|name|>(p1: T1, p2: T2, ...): T = <|mockname|>(p1, p2, ...)
      def methodDef(m: Symbol, methodType: Type, body: Tree): DefDef = {
        val params = buildParams(methodType)
        DefDef(
          overrideIfNecessary(m),
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
          case _ => ctx.abort(ctx.enclosingPosition, 
              s"ScalaMock: Don't know how to handle ${methodType.getClass}. Please open a ticket at https://github.com/paulbutcher/ScalaMock/issues")
        }
      }
      
      def forwarderImpl(m: Symbol) = {
        val mt = m.typeSignatureIn(typeToMock)
        if (isStable(m)) {
          ValDef(
            Modifiers(), 
            newTermName(m.name.toString), 
            TypeTree(mt), 
            TypeApply(
              Select(
                Literal(Constant(null)), 
                newTermName("asInstanceOf")),
              List(Ident(mt.typeSymbol))))
        } else {
          val body = Apply(
                       Select(Select(This(anon), mockFunctionName(m)), newTermName("apply")),
                       paramss(mt).flatten map { p => Ident(newTermName(p.name.toString)) })
          methodImpl(m, mt, body)
        }
      }

      def mockFunctionName(m: Symbol) = {
        val method = typeToMock.member(m.name)
        newTermName("mock$"+ m.name +"$"+ method.asTermSymbol.alternatives.indexOf(m))
      }
      
      def mockParamType(t: Type): Tree = {
        if (t.typeSymbol hasFlag PARAM)
          Ident(staticClass("scala.Any"))
        else
          paramType(t)
      }  
      
      // val <|mockname|> = new MockFunctionN[T1, T2, ..., R](factory, '<|name|>)
      def mockMethod(m: Symbol): ValDef = {
        val mt = m.typeSignatureIn(typeToMock)
        val clazz = classTag(paramCount(mt))
        val types = (paramTypes(mt) map mockParamType _) :+ mockParamType(finalResultType(mt))
        ValDef(Modifiers(),
          mockFunctionName(m), 
          TypeTree(), 
          Apply(
            Select(
              New(
                AppliedTypeTree(
                  Ident(clazz.tpe.typeSymbol),
                  types)),
              newTermName("<init>")),
            List(
              factory.tree, 
              Apply(
                Select(Select(Ident(newTermName("scala")), newTermName("Symbol")), newTermName("apply")),
                List(Literal(Constant(m.name.toString)))))))
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
              Modifiers(FINAL), 
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

      val methodsToMock = membersNotInObject filter { m => 
        m.isMethod && (!(isStable(m) || isAccessor(m)) || m.hasFlag(DEFERRED))
      }
      val forwarders = methodsToMock map forwarderImpl _
      val mocks = methodsToMock map mockMethod _
      val members = forwarders ++ mocks
      
      val result = castTo(anonClass(List(TypeTree(typeToMock)), members), typeToMock)
      
//      println("------------")
//      println(showRaw(result))
//      println("------------")
//      println(show(result))
//      println("------------")
  
      ctx.Expr(result)
    }
  }
  
  // Given something of the structure <|o.m _|> where o is a mock object
  // and m is a method, find the corresponding MockFunction instance
  def findMockFunction[F: c.TypeTag, M: c.TypeTag](c: Context)(f: c.Expr[F], actuals: List[c.universe.Type]): c.Expr[M] = {
    import c.universe._
    
    def mockFunctionName(name: Name, t: Type) = {
      val method = t.member(name)
      if (method.isOverloaded) {
        val term = method.asTermSymbol 
        val m = term.resolveOverloaded(NoPrefix, List(), actuals)
        "mock$"+ name +"$"+ term.alternatives.indexOf(m)
      } else {
        "mock$"+ name +"$0"
      }    
    }
    
    def findApplication(tree: Tree): (Tree, Name) = tree match {
      case Select(o, n) => (o, n)
      case Block(_, t) => findApplication(t)
      case Typed(t, _) => findApplication(t)
      case Function(_, t) => findApplication(t)
      case Apply(t, _) => findApplication(t)
      case TypeApply(t, _) => findApplication(t)
      case _ => c.abort(c.enclosingPosition, 
          s"ScalaMock: Unrecognised structure: ${showRaw(tree)}. Please open a ticket at https://github.com/paulbutcher/ScalaMock/issues")
    }

    val (obj, name) = findApplication(f.tree)
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
        List(TypeTree(typeTag[M].tpe))))
  }

  def toMockFunction0[R: c.TypeTag](c: Context)(f: c.Expr[() => R]) =
    findMockFunction[() => R, MockFunction0[R]](c)(f, List())

  def toMockFunction1[T1: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[T1 => R]) =
    findMockFunction[T1 => R, MockFunction1[T1, R]](c)(f, List(c.typeTag[T1].tpe))

  def toMockFunction2[T1: c.TypeTag, T2: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2) => R]) =
    findMockFunction[(T1, T2) => R, MockFunction2[T1, T2, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe))

  def toMockFunction3[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3) => R]) =
    findMockFunction[(T1, T2, T3) => R, MockFunction3[T1, T2, T3, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe))

  def toMockFunction4[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4) => R]) =
    findMockFunction[(T1, T2, T3, T4) => R, MockFunction4[T1, T2, T3, T4, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe))

  def toMockFunction5[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, T5: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5) => R]) =
    findMockFunction[(T1, T2, T3, T4, T5) => R, MockFunction5[T1, T2, T3, T4, T5, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe, c.typeTag[T5].tpe))

  def toMockFunction6[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, T5: c.TypeTag, T6: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6) => R]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6) => R, MockFunction6[T1, T2, T3, T4, T5, T6, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe, c.typeTag[T5].tpe, c.typeTag[T6].tpe))

  def toMockFunction7[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, T5: c.TypeTag, T6: c.TypeTag, T7: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7) => R]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7) => R, MockFunction7[T1, T2, T3, T4, T5, T6, T7, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe, c.typeTag[T5].tpe, c.typeTag[T6].tpe, c.typeTag[T7].tpe))

  def toMockFunction8[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, T5: c.TypeTag, T6: c.TypeTag, T7: c.TypeTag, T8: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8) => R]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8) => R, MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe, c.typeTag[T5].tpe, c.typeTag[T6].tpe, c.typeTag[T7].tpe, c.typeTag[T8].tpe))

  def toMockFunction9[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, T5: c.TypeTag, T6: c.TypeTag, T7: c.TypeTag, T8: c.TypeTag, T9: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9) => R]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9) => R, MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe, c.typeTag[T5].tpe, c.typeTag[T6].tpe, c.typeTag[T7].tpe, c.typeTag[T8].tpe, c.typeTag[T9].tpe))

  def toMockFunction1R[T1: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1*) => R]) =
    findMockFunction[(T1*) => R, MockFunction1[T1, R]](c)(f, List(c.typeTag[T1].tpe))

  def toMockFunction2R[T1: c.TypeTag, T2: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2*) => R]) =
    findMockFunction[(T1, T2*) => R, MockFunction2[T1, Seq[T2], R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[Seq[T2]].tpe))

  def toMockFunction3R[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3*) => R]) =
    findMockFunction[(T1, T2, T3*) => R, MockFunction3[T1, T2, T3, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe))

  def toMockFunction4R[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4*) => R]) =
    findMockFunction[(T1, T2, T3, T4*) => R, MockFunction4[T1, T2, T3, T4, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe))

  def toMockFunction5R[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, T5: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5*) => R]) =
    findMockFunction[(T1, T2, T3, T4, T5*) => R, MockFunction5[T1, T2, T3, T4, T5, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe, c.typeTag[T5].tpe))

  def toMockFunction6R[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, T5: c.TypeTag, T6: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6*) => R]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6*) => R, MockFunction6[T1, T2, T3, T4, T5, T6, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe, c.typeTag[T5].tpe, c.typeTag[T6].tpe))

  def toMockFunction7R[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, T5: c.TypeTag, T6: c.TypeTag, T7: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7*) => R]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7*) => R, MockFunction7[T1, T2, T3, T4, T5, T6, T7, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe, c.typeTag[T5].tpe, c.typeTag[T6].tpe, c.typeTag[T7].tpe))

  def toMockFunction8R[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, T5: c.TypeTag, T6: c.TypeTag, T7: c.TypeTag, T8: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8*) => R]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8*) => R, MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe, c.typeTag[T5].tpe, c.typeTag[T6].tpe, c.typeTag[T7].tpe, c.typeTag[T8].tpe))

  def toMockFunction9R[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, T5: c.TypeTag, T6: c.TypeTag, T7: c.TypeTag, T8: c.TypeTag, T9: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9*) => R]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9*) => R, MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe, c.typeTag[T5].tpe, c.typeTag[T6].tpe, c.typeTag[T7].tpe, c.typeTag[T8].tpe, c.typeTag[T9].tpe))

  def toStubFunction0[R: c.TypeTag](c: Context)(f: c.Expr[() => R]) =
    findMockFunction[() => R, StubFunction0[R]](c)(f, List())

  def toStubFunction1[T1: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[T1 => R]) =
    findMockFunction[T1 => R, StubFunction1[T1, R]](c)(f, List(c.typeTag[T1].tpe))

  def toStubFunction2[T1: c.TypeTag, T2: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2) => R]) =
    findMockFunction[(T1, T2) => R, StubFunction2[T1, T2, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe))

  def toStubFunction3[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3) => R]) =
    findMockFunction[(T1, T2, T3) => R, StubFunction3[T1, T2, T3, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe))

  def toStubFunction4[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4) => R]) =
    findMockFunction[(T1, T2, T3, T4) => R, StubFunction4[T1, T2, T3, T4, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe))

  def toStubFunction5[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, T5: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5) => R]) =
    findMockFunction[(T1, T2, T3, T4, T5) => R, StubFunction5[T1, T2, T3, T4, T5, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe, c.typeTag[T5].tpe))

  def toStubFunction6[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, T5: c.TypeTag, T6: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6) => R]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6) => R, StubFunction6[T1, T2, T3, T4, T5, T6, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe, c.typeTag[T5].tpe, c.typeTag[T6].tpe))

  def toStubFunction7[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, T5: c.TypeTag, T6: c.TypeTag, T7: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7) => R]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7) => R, StubFunction7[T1, T2, T3, T4, T5, T6, T7, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe, c.typeTag[T5].tpe, c.typeTag[T6].tpe, c.typeTag[T7].tpe))

  def toStubFunction8[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, T5: c.TypeTag, T6: c.TypeTag, T7: c.TypeTag, T8: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8) => R]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8) => R, StubFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe, c.typeTag[T5].tpe, c.typeTag[T6].tpe, c.typeTag[T7].tpe, c.typeTag[T8].tpe))

  def toStubFunction9[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, T5: c.TypeTag, T6: c.TypeTag, T7: c.TypeTag, T8: c.TypeTag, T9: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9) => R]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9) => R, StubFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe, c.typeTag[T5].tpe, c.typeTag[T6].tpe, c.typeTag[T7].tpe, c.typeTag[T8].tpe, c.typeTag[T9].tpe))

  def toStubFunction1R[T1: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1*) => R]) =
    findMockFunction[(T1*) => R, StubFunction1[T1, R]](c)(f, List(c.typeTag[T1].tpe))

  def toStubFunction2R[T1: c.TypeTag, T2: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2*) => R]) =
    findMockFunction[(T1, T2*) => R, StubFunction2[T1, Seq[T2], R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[Seq[T2]].tpe))

  def toStubFunction3R[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3*) => R]) =
    findMockFunction[(T1, T2, T3*) => R, StubFunction3[T1, T2, T3, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe))

  def toStubFunction4R[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4*) => R]) =
    findMockFunction[(T1, T2, T3, T4*) => R, StubFunction4[T1, T2, T3, T4, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe))

  def toStubFunction5R[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, T5: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5*) => R]) =
    findMockFunction[(T1, T2, T3, T4, T5*) => R, StubFunction5[T1, T2, T3, T4, T5, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe, c.typeTag[T5].tpe))

  def toStubFunction6R[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, T5: c.TypeTag, T6: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6*) => R]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6*) => R, StubFunction6[T1, T2, T3, T4, T5, T6, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe, c.typeTag[T5].tpe, c.typeTag[T6].tpe))

  def toStubFunction7R[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, T5: c.TypeTag, T6: c.TypeTag, T7: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7*) => R]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7*) => R, StubFunction7[T1, T2, T3, T4, T5, T6, T7, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe, c.typeTag[T5].tpe, c.typeTag[T6].tpe, c.typeTag[T7].tpe))

  def toStubFunction8R[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, T5: c.TypeTag, T6: c.TypeTag, T7: c.TypeTag, T8: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8*) => R]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8*) => R, StubFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe, c.typeTag[T5].tpe, c.typeTag[T6].tpe, c.typeTag[T7].tpe, c.typeTag[T8].tpe))

  def toStubFunction9R[T1: c.TypeTag, T2: c.TypeTag, T3: c.TypeTag, T4: c.TypeTag, T5: c.TypeTag, T6: c.TypeTag, T7: c.TypeTag, T8: c.TypeTag, T9: c.TypeTag, R: c.TypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9*) => R]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9*) => R, StubFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]](c)(f, List(c.typeTag[T1].tpe, c.typeTag[T2].tpe, c.typeTag[T3].tpe, c.typeTag[T4].tpe, c.typeTag[T5].tpe, c.typeTag[T6].tpe, c.typeTag[T7].tpe, c.typeTag[T8].tpe, c.typeTag[T9].tpe))
}