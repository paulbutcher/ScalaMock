package org.scalamock.clazz

import scala.quoted.*
import org.scalamock.context.MockContext

import scala.annotation.tailrec
private[clazz] class Utils(using val quotes: Quotes):
  import quotes.reflect.*

  extension (tpe: TypeRepr)
    def isPathDependent(ownerSymbol: Symbol): Boolean =
      @tailrec
      def loop(currentTpe: TypeRepr, names: List[String]): Boolean =
        currentTpe match
          case AppliedType(inner, appliedTypes) => loop(inner, names)
          case TypeRef(inner, name) if name == ownerSymbol.name && names.nonEmpty => true
          case TypeRef(inner, name) => loop(inner, name :: names)
          case _ => false
      loop(tpe, Nil)

    def pathDependentOverride(ownerSymbol: Symbol, newOwnerSymbol: Symbol, applyTypes: Boolean): TypeRepr =
      @tailrec
      def loop(currentTpe: TypeRepr, names: List[(String, List[TypeRepr])], appliedTypes: List[TypeRepr]): TypeRepr =
        currentTpe match
          case AppliedType(inner, appliedTypes) =>
            loop(inner, names, appliedTypes)

          case TypeRef(inner, name) if name == ownerSymbol.name && names.nonEmpty =>
            names.foldLeft[TypeRepr](This(newOwnerSymbol).tpe) { case (tpe, (name, appliedTypes)) =>
              tpe
                .select(tpe.typeSymbol.typeMember(name))
                .appliedTo(appliedTypes.filter(_ => applyTypes))
            }

          case TypeRef(inner, name) =>
            loop(inner, name -> appliedTypes :: names, Nil)

          case other =>
            tpe

      if (ownerSymbol == newOwnerSymbol)
        tpe
      else
        loop(tpe, Nil, Nil)


    def mapParamRefWithWildcard: TypeRepr =
      tpe match
        case ParamRef(PolyType(_, bounds, _), idx) =>
          bounds(idx)
        case AppliedType(tycon, args) =>
          tycon.appliedTo(args.map(_.mapParamRefWithWildcard))
        case _ =>
          tpe

    def resolveParamRefs(resType: TypeRepr, methodArgs: List[List[Tree]]) =
      def loop(baseBindings: TypeRepr, typeRepr: TypeRepr): TypeRepr =
        typeRepr match
          case pr@ParamRef(bindings, idx) if bindings == baseBindings =>
            methodArgs.head(idx).asInstanceOf[TypeTree].tpe

          case AppliedType(tycon, args) =>
            AppliedType(tycon, args.map(arg => loop(baseBindings, arg)))

          case other => other

      tpe match
        case pt: PolyType => loop(pt, resType)
        case _ => resType


    def collectTypes: List[TypeRepr] =
      def loop(currentTpe: TypeRepr, params: List[TypeRepr]): List[TypeRepr] =
        currentTpe match
          case PolyType(_, _, res)          => loop(res, Nil)
          case MethodType(_, argTypes, res) => argTypes ++ loop(res, params)
          case other                        => List(other)
      loop(tpe, Nil)

  case class MockableDefinition(idx: Int, symbol: Symbol, ownerTpe: TypeRepr):
    val mockValName = s"mock$$${symbol.name}$$$idx"
    val tpe = ownerTpe.memberType(symbol)
    private val rawTypes = tpe.widen.collectTypes
    val parameterTypes = prepareTypesFor(ownerTpe.typeSymbol).map(_.tpe).init

    def resTypeWithPathDependentOverrideFor(classSymbol: Symbol): TypeRepr =
      rawTypes.last.pathDependentOverride(ownerTpe.typeSymbol, classSymbol, applyTypes = true)

    def tpeWithSubstitutedPathDependentFor(classSymbol: Symbol): TypeRepr =
      val pathDependentTypes = rawTypes.filter(_.isPathDependent(ownerTpe.typeSymbol))
      val pdUpdated = pathDependentTypes.map(_.pathDependentOverride(ownerTpe.typeSymbol, classSymbol, applyTypes = false))
      tpe.substituteTypes(pathDependentTypes.map(_.typeSymbol), pdUpdated)

    def prepareTypesFor(classSymbol: Symbol) = rawTypes
      .map(_.pathDependentOverride(ownerTpe.typeSymbol, classSymbol, applyTypes = true))
      .map { typeRepr =>
        val adjusted =
          typeRepr.widen.mapParamRefWithWildcard match
            case TypeBounds(lower, upper) => upper
            case AppliedType(TypeRef(_, "<repeated>"), elemTyps) =>
              TypeRepr.typeConstructorOf(classOf[Seq[_]]).appliedTo(elemTyps)
            case other => other
        adjusted.asType match
          case '[t] => TypeTree.of[t]
    }

  object MockableDefinitions:
    def find(tpe: TypeRepr, name: String, paramTypes: List[TypeRepr], appliedTypes: List[TypeRepr]): String =
      def appliedTypesMatch(method: MockableDefinition, appliedTypes: List[TypeRepr]): Boolean =
        method.tpe match
          case poly: PolyType => poly.paramTypes.lengthCompare(appliedTypes) == 0
          case _ => appliedTypes.isEmpty

      def typesMatch(method: MockableDefinition, paramTypes: List[TypeRepr]): Boolean =
        paramTypes.lengthCompare(method.parameterTypes) == 0 &&
          paramTypes.zip(method.parameterTypes).forall(_ <:< _)

      val method = MockableDefinitions(tpe)
        .filter(m => m.symbol.name == name && typesMatch(m, paramTypes) && appliedTypesMatch(m, appliedTypes))
        .sortWith((a, b) => a.parameterTypes.zip(b.parameterTypes).forall(_ <:< _))
        .headOption
        .getOrElse(report.errorAndAbort(s"Method with such signature not found"))

      method.mockValName


    def apply(tpe: TypeRepr): List[MockableDefinition] =
      val methods = (tpe.typeSymbol.methodMembers.toSet -- TypeRepr.of[Object].typeSymbol.methodMembers).toList
        .filter(sym => !sym.flags.is(Flags.Private) && !sym.flags.is(Flags.Final))
        .filterNot(sym => tpe.memberType(sym) match
          case defaultParam @ ByNameType(AnnotatedType(_, Apply(Select(New(Inferred()), "<init>"), Nil))) => true
          case _ => false
        )
        .zipWithIndex
        .map((sym, idx) => MockableDefinition(idx, sym, tpe))

      val vals = tpe.typeSymbol.fieldMembers
        .filter(_.flags.is(Flags.Deferred))
        .map(sym => MockableDefinition(0, sym, tpe))
      methods ++ vals

