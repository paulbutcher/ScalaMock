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

trait Mock {
  import language.experimental.macros
  import language.implicitConversions
  
  def mock[T](implicit factory: MockFactoryBase) = macro MockImpl.mock[T]
  
  implicit def toMockFunction0[R: Defaultable](f: () => R) = macro MockImpl.toMockFunction0[R]
  implicit def toMockFunction1[T1, R: Defaultable](f: T1 => R) = macro MockImpl.toMockFunction1[T1, R]
  implicit def toMockFunction2[T1, T2, R: Defaultable](f: (T1, T2) => R) = macro MockImpl.toMockFunction2[T1, T2, R]
  implicit def toMockFunction3[T1, T2, T3, R: Defaultable](f: (T1, T2, T3) => R) = macro MockImpl.toMockFunction3[T1, T2, T3, R]
  implicit def toMockFunction4[T1, T2, T3, T4, R: Defaultable](f: (T1, T2, T3, T4) => R) = macro MockImpl.toMockFunction4[T1, T2, T3, T4, R]
  implicit def toMockFunction5[T1, T2, T3, T4, T5, R: Defaultable](f: (T1, T2, T3, T4, T5) => R) = macro MockImpl.toMockFunction5[T1, T2, T3, T4, T5, R]
  implicit def toMockFunction6[T1, T2, T3, T4, T5, T6, R: Defaultable](f: (T1, T2, T3, T4, T5, T6) => R) = macro MockImpl.toMockFunction6[T1, T2, T3, T4, T5, T6, R]
  implicit def toMockFunction7[T1, T2, T3, T4, T5, T6, T7, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7) => R) = macro MockImpl.toMockFunction7[T1, T2, T3, T4, T5, T6, T7, R]
  implicit def toMockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8) => R) = macro MockImpl.toMockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R]
  implicit def toMockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => R) = macro MockImpl.toMockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]

  implicit def toMockFunction1R[T1, R: Defaultable](f: (T1*) => R) = macro MockImpl.toMockFunction1R[T1, R]
  implicit def toMockFunction2R[T1, T2, R: Defaultable](f: (T1, T2*) => R) = macro MockImpl.toMockFunction2R[T1, T2, R]
  implicit def toMockFunction3R[T1, T2, T3, R: Defaultable](f: (T1, T2, T3*) => R) = macro MockImpl.toMockFunction3R[T1, T2, T3, R]
  implicit def toMockFunction4R[T1, T2, T3, T4, R: Defaultable](f: (T1, T2, T3, T4*) => R) = macro MockImpl.toMockFunction4R[T1, T2, T3, T4, R]
  implicit def toMockFunction5R[T1, T2, T3, T4, T5, R: Defaultable](f: (T1, T2, T3, T4, T5*) => R) = macro MockImpl.toMockFunction5R[T1, T2, T3, T4, T5, R]
  implicit def toMockFunction6R[T1, T2, T3, T4, T5, T6, R: Defaultable](f: (T1, T2, T3, T4, T5, T6*) => R) = macro MockImpl.toMockFunction6R[T1, T2, T3, T4, T5, T6, R]
  implicit def toMockFunction7R[T1, T2, T3, T4, T5, T6, T7, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7*) => R) = macro MockImpl.toMockFunction7R[T1, T2, T3, T4, T5, T6, T7, R]
  implicit def toMockFunction8R[T1, T2, T3, T4, T5, T6, T7, T8, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8*) => R) = macro MockImpl.toMockFunction8R[T1, T2, T3, T4, T5, T6, T7, T8, R]
  implicit def toMockFunction9R[T1, T2, T3, T4, T5, T6, T7, T8, T9, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9*) => R) = macro MockImpl.toMockFunction9R[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]

  def stub[T](implicit factory: MockFactoryBase) = macro MockImpl.stub[T]

  implicit def toStubFunction0[R: Defaultable](f: () => R) = macro MockImpl.toStubFunction0[R]
  implicit def toStubFunction1[T1,  R: Defaultable](f: T1 => R) = macro MockImpl.toStubFunction1[T1, R]
  implicit def toStubFunction2[T1, T2, R: Defaultable](f: (T1, T2) => R) = macro MockImpl.toStubFunction2[T1, T2, R]
  implicit def toStubFunction3[T1, T2, T3, R: Defaultable](f: (T1, T2, T3) => R) = macro MockImpl.toStubFunction3[T1, T2, T3, R]
  implicit def toStubFunction4[T1, T2, T3, T4, R: Defaultable](f: (T1, T2, T3, T4) => R) = macro MockImpl.toStubFunction4[T1, T2, T3, T4, R]
  implicit def toStubFunction5[T1, T2, T3, T4, T5, R: Defaultable](f: (T1, T2, T3, T4, T5) => R) = macro MockImpl.toStubFunction5[T1, T2, T3, T4, T5, R]
  implicit def toStubFunction6[T1, T2, T3, T4, T5, T6, R: Defaultable](f: (T1, T2, T3, T4, T5, T6) => R) = macro MockImpl.toStubFunction6[T1, T2, T3, T4, T5, T6, R]
  implicit def toStubFunction7[T1, T2, T3, T4, T5, T6, T7, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7) => R) = macro MockImpl.toStubFunction7[T1, T2, T3, T4, T5, T6, T7, R]
  implicit def toStubFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8) => R) = macro MockImpl.toStubFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R]
  implicit def toStubFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => R) = macro MockImpl.toStubFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]

  implicit def toStubFunction1R[T1, R: Defaultable](f: (T1*) => R) = macro MockImpl.toStubFunction1R[T1, R]
  implicit def toStubFunction2R[T1, T2, R: Defaultable](f: (T1, T2*) => R) = macro MockImpl.toStubFunction2R[T1, T2, R]
  implicit def toStubFunction3R[T1, T2, T3, R: Defaultable](f: (T1, T2, T3*) => R) = macro MockImpl.toStubFunction3R[T1, T2, T3, R]
  implicit def toStubFunction4R[T1, T2, T3, T4, R: Defaultable](f: (T1, T2, T3, T4*) => R) = macro MockImpl.toStubFunction4R[T1, T2, T3, T4, R]
  implicit def toStubFunction5R[T1, T2, T3, T4, T5, R: Defaultable](f: (T1, T2, T3, T4, T5*) => R) = macro MockImpl.toStubFunction5R[T1, T2, T3, T4, T5, R]
  implicit def toStubFunction6R[T1, T2, T3, T4, T5, T6, R: Defaultable](f: (T1, T2, T3, T4, T5, T6*) => R) = macro MockImpl.toStubFunction6R[T1, T2, T3, T4, T5, T6, R]
  implicit def toStubFunction7R[T1, T2, T3, T4, T5, T6, T7, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7*) => R) = macro MockImpl.toStubFunction7R[T1, T2, T3, T4, T5, T6, T7, R]
  implicit def toStubFunction8R[T1, T2, T3, T4, T5, T6, T7, T8, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8*) => R) = macro MockImpl.toStubFunction8R[T1, T2, T3, T4, T5, T6, T7, T8, R]
  implicit def toStubFunction9R[T1, T2, T3, T4, T5, T6, T7, T8, T9, R: Defaultable](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9*) => R) = macro MockImpl.toStubFunction9R[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]
}

object MockImpl {
  import reflect.macros.Context
  
  def mock[T: c.WeakTypeTag](c: Context)(factory: c.Expr[MockFactoryBase]): c.Expr[T] = {
    val maker = MockMaker[T](c)(factory, stub = false)

    maker.make
  }
  
  def stub[T: c.WeakTypeTag](c: Context)(factory: c.Expr[MockFactoryBase]): c.Expr[T] = {
    val maker = MockMaker[T](c)(factory, stub = true)

    maker.make
  }

  class Utils[C <: Context](val ctx2: C) { // ctx2 to avoid clash with ctx in MockMaker (eugh!)
    import ctx2.universe._

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
      paramss(methodType).flatten map { _.typeSignature }
  }
  
  def MockMaker[T: c.WeakTypeTag](c: Context)(factory: c.Expr[MockFactoryBase], stub: Boolean) = {
    val m = new MockMaker[c.type](c)
    new m.MockMakerInner[T](factory, stub)
  }
  
  //! TODO - get rid of this nasty two-stage construction when https://issues.scala-lang.org/browse/SI-5521 is fixed
  class MockMaker[C <: Context](val ctx: C) {
    class MockMakerInner[T: ctx.WeakTypeTag](factory: ctx.Expr[MockFactoryBase], stub: Boolean) {
      import ctx.mirror._
      import ctx.universe._
      import Flag._
      import definitions._
      import language.reflectiveCalls

      val utils = new Utils[ctx.type](ctx)
      import utils._

      def mockFunctionClass(paramCount: Int): Type = paramCount match {
        case 0 => typeOf[MockFunction0[_]]
        case 1 => typeOf[MockFunction1[_, _]]
        case 2 => typeOf[MockFunction2[_, _, _]]
        case 3 => typeOf[MockFunction3[_, _, _, _]]
        case 4 => typeOf[MockFunction4[_, _, _, _, _]]
        case 5 => typeOf[MockFunction5[_, _, _, _, _, _]]
        case 6 => typeOf[MockFunction6[_, _, _, _, _, _, _]]
        case 7 => typeOf[MockFunction7[_, _, _, _, _, _, _, _]]
        case 8 => typeOf[MockFunction8[_, _, _, _, _, _, _, _, _]]
        case 9 => typeOf[MockFunction9[_, _, _, _, _, _, _, _, _, _]]
        case _ => ctx.abort(ctx.enclosingPosition, "ScalaMock: Can't handle methods with more than 9 parameters (yet)")
      }
      
      def stubFunctionClass(paramCount: Int): Type = paramCount match {
        case 0 => typeOf[StubFunction0[_]]
        case 1 => typeOf[StubFunction1[_, _]]
        case 2 => typeOf[StubFunction2[_, _, _]]
        case 3 => typeOf[StubFunction3[_, _, _, _]]
        case 4 => typeOf[StubFunction4[_, _, _, _, _]]
        case 5 => typeOf[StubFunction5[_, _, _, _, _, _]]
        case 6 => typeOf[StubFunction6[_, _, _, _, _, _, _]]
        case 7 => typeOf[StubFunction7[_, _, _, _, _, _, _, _]]
        case 8 => typeOf[StubFunction8[_, _, _, _, _, _, _, _, _]]
        case 9 => typeOf[StubFunction9[_, _, _, _, _, _, _, _, _, _]]
        case _ => ctx.abort(ctx.enclosingPosition, "ScalaMock: Can't handle methods with more than 9 parameters (yet)")
      }
      
      def classType(paramCount: Int) = if (stub) stubFunctionClass(paramCount) else mockFunctionClass(paramCount)
      
      def isPathDependentThis(t: Type): Boolean = t match {
        case TypeRef(pre, _, _) => isPathDependentThis(pre)
        case ThisType(tpe) => tpe == typeToMock.typeSymbol
        case _ => false
      }
      
      def paramType(t: Type) = t match {
        case TypeRef(pre, sym, args) if sym == JavaRepeatedParamClass =>
          TypeTree(TypeRef(pre, RepeatedParamClass, args))
        case TypeRef(pre, sym, args) if isPathDependentThis(t) =>
          AppliedTypeTree(Ident(newTypeName(sym.name.toString)), args map TypeTree _)
        case _ =>
          TypeTree(t)
      }
  
      def methodsNotInObject =
        typeToMock.members filter (m => m.isMethod && !isMemberOfObject(m)) map (_.asMethod)
      
      //! TODO - This is a hack, but it's unclear what it should be instead. See
      //! https://groups.google.com/d/topic/scala-user/n11V6_zI5go/discussion
      def resolvedType(m: Symbol) =
        m.typeSignatureIn(SuperType(ThisType(typeToMock.typeSymbol), typeToMock))
        
      def buildParams(methodType: Type) =
        paramss(methodType) map { params =>
          params map { p =>
            ValDef(
              Modifiers(PARAM | (if (p.isImplicit) IMPLICIT else NoFlags)),
              newTermName(p.name.toString),
              paramType(p.typeSignature),
              EmptyTree)
          }
        }
      
      // def <|name|>(p1: T1, p2: T2, ...): T = <|mockname|>(p1, p2, ...)
      def methodDef(m: Symbol, methodType: Type, body: Tree): DefDef = {
        val params = buildParams(methodType)
        DefDef(
          Modifiers(OVERRIDE),
          m.name, 
          m.asMethod.typeParams map TypeDef _, 
          params,
          paramType(finalResultType(methodType)),
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
        val mt = resolvedType(m)
        if (m.asTerm.isStable) {
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
        newTermName("mock$"+ m.name +"$"+ method.asTerm.alternatives.indexOf(m))
      }
      
      // val <|mockname|> = new MockFunctionN[T1, T2, ..., R](factory, '<|name|>)
      def mockMethod(m: Symbol): ValDef = {
        val mt = resolvedType(m)
        val clazz = classType(paramCount(mt))
        val types = (paramTypes(mt) map paramType _) :+ paramType(finalResultType(mt))
        ValDef(Modifiers(),
          mockFunctionName(m), 
          TypeTree(), 
          Apply(
            Select(
              New(
                AppliedTypeTree(
                  Ident(clazz.typeSymbol),
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
  
      // new <|typeToMock|> { <|members|> }
      def anonClass(members: List[Tree]) =
        Block(
          List(
            ClassDef(
              Modifiers(FINAL), 
              anon,
              List(),
              Template(
                List(TypeTree(typeToMock)), 
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
  
      val typeToMock = weakTypeOf[T]
      val anon = newTypeName("$anon") 
      val methodsToMock = methodsNotInObject.filter( m =>
          !m.isConstructor && (!(m.isStable || m.isAccessor) ||
            m.asInstanceOf[reflect.internal.HasFlags].isDeferred) //! TODO - stop using internal if/when this gets into the API
        ).toList
      val forwarders = methodsToMock map forwarderImpl _
      val mocks = methodsToMock map mockMethod _
      val members = forwarders ++ mocks
      
      def make() = {
        val result = castTo(anonClass(members), typeToMock)

       // println("------------")
       // println(showRaw(result))
       // println("------------")
       // println(show(result))
       // println("------------")
    
        ctx.Expr(result)
      }
    }
  }
  
  // Given something of the structure <|o.m _|> where o is a mock object
  // and m is a method, find the corresponding MockFunction instance
  def findMockFunction[F: c.WeakTypeTag, M: c.WeakTypeTag](c: Context)(f: c.Expr[F], actuals: List[c.universe.Type]): c.Expr[M] = {
    import c.universe._

    val utils = new Utils[c.type](c)
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
    def resolveOverloaded(method: TermSymbol): Symbol = {
      method.alternatives find { m => 
        paramTypes(m.typeSignature) sameElements actuals
      } getOrElse {
        reportError(s"Unable to resolve overloaded method ${method.name}")
      }
    }
    
    def mockFunctionName(name: Name, t: Type) = {
      val method = t.member(name).asTerm
      if (method.isOverloaded) {
        val m = resolveOverloaded(method)
        "mock$"+ name +"$"+ method.alternatives.indexOf(m)
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
      case _ => reportError(
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
        List(TypeTree(weakTypeOf[M]))))
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

  def toMockFunction1R[T1: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1*) => R])(evidence$11: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1*) => R, MockFunction1[T1, R]](c)(f, List(c.weakTypeOf[T1]))

  def toMockFunction2R[T1: c.WeakTypeTag, T2: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2*) => R])(evidence$12: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2*) => R, MockFunction2[T1, Seq[T2], R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[Seq[T2]]))

  def toMockFunction3R[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3*) => R])(evidence$13: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3*) => R, MockFunction3[T1, T2, T3, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3]))

  def toMockFunction4R[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4*) => R])(evidence$14: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4*) => R, MockFunction4[T1, T2, T3, T4, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4]))

  def toMockFunction5R[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5*) => R])(evidence$15: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5*) => R, MockFunction5[T1, T2, T3, T4, T5, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5]))

  def toMockFunction6R[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6*) => R])(evidence$16: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6*) => R, MockFunction6[T1, T2, T3, T4, T5, T6, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6]))

  def toMockFunction7R[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7*) => R])(evidence$17: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7*) => R, MockFunction7[T1, T2, T3, T4, T5, T6, T7, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7]))

  def toMockFunction8R[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8*) => R])(evidence$18: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8*) => R, MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8]))

  def toMockFunction9R[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9*) => R])(evidence$19: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9*) => R, MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9]))

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

  def toStubFunction1R[T1: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1*) => R])(evidence$30: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1*) => R, StubFunction1[T1, R]](c)(f, List(c.weakTypeOf[T1]))

  def toStubFunction2R[T1: c.WeakTypeTag, T2: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2*) => R])(evidence$31: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2*) => R, StubFunction2[T1, Seq[T2], R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[Seq[T2]]))

  def toStubFunction3R[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3*) => R])(evidence$32: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3*) => R, StubFunction3[T1, T2, T3, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3]))

  def toStubFunction4R[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4*) => R])(evidence$33: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4*) => R, StubFunction4[T1, T2, T3, T4, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4]))

  def toStubFunction5R[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5*) => R])(evidence$34: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5*) => R, StubFunction5[T1, T2, T3, T4, T5, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5]))

  def toStubFunction6R[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6*) => R])(evidence$35: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6*) => R, StubFunction6[T1, T2, T3, T4, T5, T6, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6]))

  def toStubFunction7R[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7*) => R])(evidence$36: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7*) => R, StubFunction7[T1, T2, T3, T4, T5, T6, T7, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7]))

  def toStubFunction8R[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8*) => R])(evidence$37: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8*) => R, StubFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8]))

  def toStubFunction9R[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(f: c.Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9*) => R])(evidence$38: c.Expr[Defaultable[R]]) =
    findMockFunction[(T1, T2, T3, T4, T5, T6, T7, T8, T9*) => R, StubFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]](c)(f, List(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9]))
}