package org.scalamock.clazz

import scala.quoted.*

import scala.annotation.{experimental, tailrec}
private[clazz] class Utils(using val quotes: Quotes):
  import quotes.reflect.*

  extension (tpe: TypeRepr)
    def collectInnerTypes(ownerSymbol: Symbol): List[TypeRepr] =
      def loop(currentTpe: TypeRepr, names: List[String]): List[TypeRepr] =
        currentTpe match
          case AppliedType(inner, appliedTypes) => loop(inner, names) ++ appliedTypes.flatMap(_.collectInnerTypes(ownerSymbol))
          case TypeRef(inner, name) if name == ownerSymbol.name && names.nonEmpty => List(tpe)
          case TypeRef(inner, name) => loop(inner, name :: names)
          case _ => Nil

      loop(tpe, Nil)

    def innerTypeOverride(ownerSymbol: Symbol, newOwnerSymbol: Symbol, applyTypes: Boolean): TypeRepr =
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

    def resolveAndOrTypeParamRefs: TypeRepr =
      tpe match {
        case AndType(left @ (_: ParamRef | _: AppliedType), right @ (_: ParamRef | _: AppliedType)) =>
          TypeRepr.of[Any]
        case AndType(left @ (_: ParamRef | _: AppliedType), right) =>
          right.resolveAndOrTypeParamRefs
        case AndType(left, right @ (_: ParamRef | _: AppliedType)) =>
          left.resolveAndOrTypeParamRefs
        case OrType(_: ParamRef | _: AppliedType, _) =>
          TypeRepr.of[Any]
        case OrType(_, _: ParamRef | _: AppliedType) =>
          TypeRepr.of[Any]
        case other =>
          other
      }

    @experimental
    def resolveParamRefs(resType: TypeRepr, methodArgs: List[List[Tree]]) =
      tpe match
        case baseBindings: PolyType =>
          def loop(typeRepr: TypeRepr): TypeRepr =
            typeRepr match
              case pr@ParamRef(bindings, idx) if bindings == baseBindings =>
                methodArgs.head(idx).asInstanceOf[TypeTree].tpe

              case AndType(left, right) =>
                AndType(loop(left), loop(right))
                
              case OrType(left, right) =>
                OrType(loop(left), loop(right))

              case AppliedType(tycon, args) =>
                AppliedType(loop(tycon), args.map(arg => loop(arg)))

              case ff @ TypeRef(ref @ ParamRef(bindings, idx), name) =>
                def getIndex(bindings: TypeRepr): Int =
                  @tailrec
                  def loop(bindings: TypeRepr, idx: Int): Int =
                    bindings match
                      case MethodType(_, _, method: MethodType) => loop(method, idx + 1)
                      case _ => idx

                  loop(bindings, 1)

                val maxIndex = methodArgs.length
                val parameterListIdx = maxIndex - getIndex(bindings)

                TypeSelect(methodArgs(parameterListIdx)(idx).asInstanceOf[Term], name).tpe

              case other => other

          loop(resType)
        case _ =>
          resType


    def collectTypes: (List[TypeRepr], TypeRepr) =
      @tailrec
      def loop(currentTpe: TypeRepr, argTypesAcc: List[List[TypeRepr]], resType: TypeRepr): (List[TypeRepr], TypeRepr) =
        currentTpe match
          case PolyType(_, _, res)          => loop(res, List.empty[TypeRepr] :: argTypesAcc, resType)
          case MethodType(_, argTypes, res) => loop(res, argTypes :: argTypesAcc, resType)
          case other                        => (argTypesAcc.reverse.flatten, other)
      loop(tpe, Nil, TypeRepr.of[Nothing])

  case class MockableDefinition(idx: Int, symbol: Symbol, ownerTpe: TypeRepr):
    val mockValName = s"mock$$${symbol.name}$$$idx"
    val tpe = ownerTpe.memberType(symbol)
    private val (rawTypes, rawResType) = tpe.widen.collectTypes
    val parameterTypes = prepareTypesFor(ownerTpe.typeSymbol).map(_.tpe).init

    def resTypeWithInnerTypesOverrideFor(classSymbol: Symbol): TypeRepr =
      updatePathDependent(rawResType, List(rawResType), classSymbol)

    def tpeWithSubstitutedInnerTypesFor(classSymbol: Symbol): TypeRepr =
      updatePathDependent(tpe, rawResType :: rawTypes, classSymbol)

    private def updatePathDependent(where: TypeRepr, types: List[TypeRepr], classSymbol: Symbol): TypeRepr =
      val pathDependentTypes = types.flatMap(_.collectInnerTypes(ownerTpe.typeSymbol))
      val pdUpdated = pathDependentTypes.map(_.innerTypeOverride(ownerTpe.typeSymbol, classSymbol, applyTypes = false))
      where.substituteTypes(pathDependentTypes.map(_.typeSymbol), pdUpdated)

    def prepareTypesFor(classSymbol: Symbol) = (rawTypes :+ rawResType)
      .map(_.innerTypeOverride(ownerTpe.typeSymbol, classSymbol, applyTypes = true))
      .map { typeRepr =>
        val adjusted =
          typeRepr.widen.mapParamRefWithWildcard.resolveAndOrTypeParamRefs match
            case TypeBounds(lower, upper) => upper
            case AppliedType(TypeRef(_, "<repeated>"), elemTyps) =>
              TypeRepr.typeConstructorOf(classOf[Seq[_]]).appliedTo(elemTyps)
            case TypeRef(_: ParamRef, _) =>
              TypeRepr.of[Any]
            case AppliedType(TypeRef(_: ParamRef, _), _) =>
              TypeRepr.of[Any]
            case other =>
              other
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
        .filter(sym =>
          !sym.flags.is(Flags.Private) &&
          !sym.flags.is(Flags.Final) &&
          !sym.flags.is(Flags.Mutable) &&
          sym.privateWithin.isEmpty &&
          !sym.name.contains("$default$")
        )
        .zipWithIndex
        .map((sym, idx) => MockableDefinition(idx, sym, tpe))

      val vals = tpe.typeSymbol.fieldMembers
        .filter(_.flags.is(Flags.Deferred))
        .map(sym => MockableDefinition(0, sym, tpe))
      methods ++ vals

