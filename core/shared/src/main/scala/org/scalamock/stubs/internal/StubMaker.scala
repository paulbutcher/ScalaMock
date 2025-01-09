package org.scalamock.stubs.internal

import org.scalamock.clazz.Utils
import org.scalamock.stubs.{StubArgumentLog, Stub, StubIO, Stubs}

import java.util.concurrent.atomic.AtomicReference
import scala.annotation.tailrec
import scala.quoted.{Expr, Quotes, Type}

private[stubs] class StubMaker(
  using Quotes
) extends Utils:
  import quotes.reflect.*

  override def newApi = true

  def newInstance[T: Type](
    collector: Expr[Stubs#Created]
  ): Expr[Stub[T]] =
    val tpe = TypeRepr.of[T]
    val parents = parentsOf[T]
    val methods = MockableDefinitions(tpe)

    val classSymbol = Symbol.newClass(
      parent = Symbol.spliceOwner,
      name = "anon",
      parents = parents.map {
        case term: Term => term.tpe
        case tpt: TypeTree => tpt.tpe
      },
      decls = classSymbol =>
        methods.flatMap { method =>
          List(
            Symbol.newVal(
              parent = classSymbol,
              name = method.callsValName,
              tpe = TypeRepr.of[AtomicReference[List[Any]]],
              flags = Flags.EmptyFlags,
              privateWithin = Symbol.noSymbol
            ),
            Symbol.newVal(
              parent = classSymbol,
              name = method.mockValName,
              tpe = TypeRepr.of[AtomicReference[Option[Any => Any]]],
              flags = Flags.EmptyFlags,
              privateWithin = Symbol.noSymbol
            ),
            if method.symbol.isValDef then
              Symbol.newVal(
                parent = classSymbol,
                name = method.symbol.name,
                tpe = method.tpeWithSubstitutedInnerTypesFor(classSymbol),
                flags = Flags.Override,
                privateWithin = Symbol.noSymbol
              )
            else
              Symbol.newMethod(
                parent = classSymbol,
                name = method.symbol.name,
                tpe = method.tpeWithSubstitutedInnerTypesFor(classSymbol),
                flags = Flags.Override,
                privateWithin = Symbol.noSymbol
              ),
          )
        } :+ Symbol.newMethod(
          parent = classSymbol,
          name = ClearStubsMethodName,
          tpe = TypeRepr.of[Unit],
          flags = Flags.EmptyFlags,
          privateWithin = Symbol.noSymbol
        ),
      selfType = None
    )

    val classDef = ClassDef(
      cls = classSymbol,
      parents = parents,
      body = methods.flatMap { method =>
        List(
          ValDef(
            classSymbol.declaredField(method.mockValName),
            Some {
              '{ new AtomicReference[Option[Any => Any]](None)}.asTerm
            }
          ),
          ValDef(
            classSymbol.declaredField(method.callsValName),
            Some {
              '{ new AtomicReference(List.empty[Any]) }.asTerm
            }
          ),
          if (method.symbol.isValDef)
            ValDef(method.symbol.overridingSymbol(classSymbol), Some('{ null }.asTerm))
          else
            DefDef(
              symbol = method.symbol.overridingSymbol(classSymbol),
              params => Some {
                '{
                  ${method.functionRef(classSymbol)}.get() match {
                    case None =>
                      throw new NotImplementedError()
                    case Some(fun) =>
                      ${
                        val args = params.map {_.collect { case term: Term => term }}.filterNot(_.isEmpty)
                        val (argsToUpdate, result) = args match
                          case Nil =>
                            ('{ () }.asTerm, '{fun(())}.asTerm)
                          case params =>
                            val tupledArgs = params.flatten match
                              case Nil => report.errorAndAbort("Unexpected error occurred, please open an issue")
                              case arg :: Nil => arg
                              case args => tupled(args)

                            val result = '{fun.apply(${tupledArgs.asExpr}.asInstanceOf)}.asTerm
                            (tupledArgs, result)

                        val updateCalls = '{
                          ${ method.callsRef(classSymbol) }.getAndUpdate(_ :+ ${ argsToUpdate.asExpr })
                          ${ method.recordCall(params) }
                        }

                        val resTpe = method.tpe
                          .prepareResType(method.resTypeWithInnerTypesOverrideFor(classSymbol), params)

                        resTpe.asType match
                          case '[res] =>
                            Expr.summon[StubIO[?]] match
                              case None =>
                                '{
                                  ${ updateCalls }
                                  ${ result.asExpr }.asInstanceOf[res]
                                }
                              case Some(io) =>
                                val ioTerm = io.asTerm
                                val ioTpe = ioTerm.tpe.select(ioTerm.tpe.typeSymbol.typeMember("Underlying"))
                                if resTpe <:< AppliedType(ioTpe, List(TypeRepr.of[Any], TypeRepr.of[Any])) then
                                  val (errorArgTpe, resArgTpe) = resTpe.dealias.typeArgs match
                                    case List(_, err, res) => (err, res)
                                    case List(err, res) => (err, res)
                                    case List(res) => (TypeRepr.of[Nothing], res)
                                    case _ => report.errorAndAbort(s"$resTpe is not a type constructor")
                                  TypeApply(
                                    Select.unique(
                                      Apply(
                                        Apply(
                                          TypeApply(
                                            Select.unique(ioTerm, "flatMap"),
                                            List(
                                              Inferred(TypeRepr.of[Nothing]),
                                              Inferred(errorArgTpe),
                                              Inferred(TypeRepr.of[Unit]),
                                              Inferred(resArgTpe)
                                            )
                                          ),
                                          List(
                                            Apply(
                                              TypeApply(Select.unique(ioTerm, "succeed"), List(TypeTree.of[Unit])),
                                              List(updateCalls.asTerm)
                                            )
                                          )
                                        ),
                                        List('{ (_: Unit) => ${ result.asExpr }.asInstanceOf[res] }.asTerm)
                                      ),
                                      "asInstanceOf"
                                    ),
                                    List(TypeTree.of[res])
                                  ).asExprOf[res]
                                else
                                  '{
                                    ${ updateCalls }
                                    ${ result.asExpr }.asInstanceOf[res]
                                  }

                      }
                  }
                }.asTerm.changeOwner(method.symbol.overridingSymbol(classSymbol))
              }
            )
        )
      } :+ DefDef(
        symbol = classSymbol.methodMember(ClearStubsMethodName).head,
        _ =>
          Some(
            Block(
              methods.map { method => '{
                ${ method.callsRef(classSymbol) }.set(Nil)
                ${ method.functionRef(classSymbol) }.set(None)
              }.asTerm },
              '{}.asTerm
            )
          )
      )
    )

    val instance = Block(
      List(classDef),
      Typed(
        Apply(
          Select(New(TypeIdent(classSymbol)), classSymbol.primaryConstructor),
          Nil
        ),
        TypeTree.of[T & scala.reflect.Selectable]
      )
    )
    '{
      ${ collector }.bind(${ instance.asExprOf[T] }.asInstanceOf[Stub[T]])
    }
  end newInstance

  def returnsMacro[F: Type, Args: Type, R: Type](
    select: Expr[F],
    returns: Expr[Args => R]
  ): Expr[Unit] =
    returnsInternal(select.asTerm, returns.asTerm, tupleTypeToList(TypeRepr.of[Args]))

  def returnsMacro[F: Type](select: Expr[F], returns: Expr[F]): Expr[Unit] =
    returnsInternal(select.asTerm, '{ (_: Unit) => ${returns}}.asTerm, Nil)

  private def returnsInternal(
    select: Term,
    returns: Term,
    argTypes: List[TypeRepr]
  ) =
    val (term, method) = searchTermWithMethod(select, argTypes)
    '{
      ${ term.asExpr }
        .asInstanceOf[scala.reflect.Selectable]
        .selectDynamic(${ Expr(method.mockValName) })
        .asInstanceOf[AtomicReference[Option[Any => Any]]]
        .set(Some(${ returns.asExpr }.asInstanceOf[Any => Any]))
    }


  def callsMacro[F: Type, Args: Type, R: Type](
    select: Expr[F]
  ): Expr[List[Args]] =
    val (term, method) = searchTermWithMethod(select.asTerm, tupleTypeToList(TypeRepr.of[Args]))
    '{
      ${ term.asExpr }
        .asInstanceOf[scala.reflect.Selectable]
        .selectDynamic(${ Expr(method.callsValName) })
        .asInstanceOf[AtomicReference[List[Args]]]
        .get()
    }



  @tailrec
  private def tupleTypeToList(tpe: TypeRepr, acc: List[TypeRepr] = Nil): List[TypeRepr] =
    tpe.asType match
      case '[h *: t] =>
        tupleTypeToList(TypeRepr.of[t], acc :+ TypeRepr.of[h])
      case '[EmptyTuple] =>
        acc
      case _ =>
        acc :+ tpe


  private def tupled(args: List[Term]) =
    args.foldRight[Term]('{ EmptyTuple }.asTerm) { (el, acc) =>
      Select
        .unique(acc, "*:")
        .appliedToTypes(List(el.tpe, acc.tpe))
        .appliedToArgs(List(el))
    }

  extension (method: MockableDefinition)
    private def callsRef(classSymbol: Symbol): Expr[AtomicReference[List[Any]]] =
      Ref(classSymbol.declaredField(method.callsValName)).asExprOf[AtomicReference[List[Any]]]

    private def functionRef(classSymbol: Symbol): Expr[AtomicReference[Option[Any => Any]]] =
      Ref(classSymbol.declaredField(method.mockValName)).asExprOf[AtomicReference[Option[Any => Any]]]

    private def recordCall(params: List[List[Tree]]): Expr[Unit] =
      Expr.summon[Stubs#CallLog] match
        case None =>
          '{}
        case Some(callLog) =>
          given Printer[TypeRepr] = Printer.TypeReprShortCode
          val typeParams = method.tpe match
            case PolyType(params, _, _) => params.mkString("[", ",", "]")
            case _ => ""

          val methodName = s"${method.ownerTpe.show}.${method.symbol.name}$typeParams"

          val args = params.map {_.collect { case term: Term => term }}.filterNot(_.isEmpty)
          val writers = args.map { args =>
            args.map { arg =>
              arg.tpe.asType match
                case '[t] =>
                  '{
                    val writer = ${Expr.summon[StubArgumentLog[t]].getOrElse(report.errorAndAbort("error"))}
                      .asInstanceOf[StubArgumentLog[Any]]
                    (${arg.asExpr}, writer)
                  }
            }
          }
          val writersExpr = Expr.ofList(writers.map(writers => Expr.ofList(writers)))
          '{ ${callLog}.write(${Expr(methodName)}, ${writersExpr}) }
