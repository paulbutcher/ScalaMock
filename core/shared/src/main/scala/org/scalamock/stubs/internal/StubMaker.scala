package org.scalamock.stubs.internal

import org.scalamock.clazz.Utils
import org.scalamock.stubs.{Stub, StubIO, Stubs}

import java.util.concurrent.atomic.AtomicReference
import scala.annotation.tailrec
import scala.quoted.{Expr, Quotes, Type}

private[stubs] class StubMaker(
  using Quotes
) extends Utils:
  import quotes.reflect.*

  override def newApi = true

  def newInstance[T: Type](
    collector: Expr[CreatedStubs]
  ): Expr[Stub[T]] =
    val tpe = TypeRepr.of[T]
    val parents = parentsOf[T]
    val methods = MockableDefinitions(tpe)
    val log = Expr.summon[Stubs#CallLog]

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
        } ::: List(
          Some(
            Symbol.newMethod(
              parent = classSymbol,
              name = ClearStubsMethodName,
              tpe = TypeRepr.of[Unit],
              flags = Flags.EmptyFlags,
              privateWithin = Symbol.noSymbol
            )
          ),
          log.map { _ =>
            Symbol.newVal(
              parent = classSymbol,
              name = UniqueStubIdx,
              tpe = TypeRepr.of[Int],
              flags = Flags.EmptyFlags,
              privateWithin = Symbol.noSymbol
            )
          }
        ).flatten,
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
                val resTpe = method.tpe.prepareResType(method.resTypeWithInnerTypesOverrideFor(classSymbol), params)
                resTpe.asType match
                  case '[res] =>
                    val ioOpt = Expr.summon[StubIO[?]].filter(_.isMatches(resTpe))
                    '{
                      ${ method.functionRef(classSymbol) }.get() match
                        case None =>
                          ${
                            ioOpt match
                              case None =>
                                '{throw new NotImplementedError()}
                              case Some(io) =>
                                '{${io}.die(new NotImplementedError())}
                          }
                        case Some(fun) =>
                          ${
                            val tupledArgs = tupled(params.flatMap {_.collect { case term: Term => term }}).asExpr
                            val result = '{fun(${tupledArgs})}
                            val updateCalls = '{
                              ${ method.callsRef(classSymbol) }.getAndUpdate(_ :+ ${ tupledArgs })
                              ${ log.fold('{}) { callLog => '{ ${callLog}.internal.write(${StubWithMethod(This(classSymbol), method).show}) } } }
                            }
                            ioOpt match
                              case None =>
                                '{
                                  ${ updateCalls }
                                  ${ result }.asInstanceOf[res]
                                }
                              case Some(io) =>
                                io.wrap(resTpe, updateCalls, result).asExprOf[res]
                          }
                    }.asTerm.changeOwner(method.symbol.overridingSymbol(classSymbol))
                }
            )
        )
      } :::
      List(
        Some(
          DefDef(
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
        ),
        log.map { log =>
          ValDef(
            symbol = classSymbol.declaredField(UniqueStubIdx),
            Some {
              '{${log}.internal.nextIdx}.asTerm
            }
          )
        }
      ).flatten
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

  private def returnsInternal(select: Term, returns: Term, argTypes: List[TypeRepr]) =
    val function = searchTermWithMethod(select, argTypes)
      .selectReflect[AtomicReference[Option[Any => Any]]](_.mockValName)
    '{ ${function}.set(Some(${ returns.asExpr }.asInstanceOf[Any => Any])) }


  def callsMacro[F: Type, Args: Type, R: Type](select: Expr[F]): Expr[List[Args]] =
    val args = searchTermWithMethod(select.asTerm, tupleTypeToList(TypeRepr.of[Args]))
      .selectReflect[AtomicReference[List[Args]]](_.callsValName)
    '{ ${args}.get() }

  def parseMethods(calls: Expr[Seq[Any]]): Expr[List[String]] =
    Expr.ofList {
      parseCalls(calls.asTerm).map {
        case select: Select =>
          searchTermWithMethod(select, Nil).show

        case Lambda(paramClause, term) =>
          searchTermWithMethod(term, paramClause.map(_.tpt.tpe)).show

        case Inlined(_, _, Lambda(paramClause, term)) =>
          searchTermWithMethod(term, paramClause.map(_.tpt.tpe)).show

        case tree =>
          report.errorAndAbort(s"Not a method selection: ${tree.show(using Printer.TreeStructure)}")
      }
    }

  @tailrec
  private def parseCalls(term: Term): List[Term] =
    term match
      case Inlined(_, _, term) =>
        parseCalls(term)

      case Typed(Repeated(List(term: Inlined), _), _) =>
        parseCalls(term)

      case Typed(Repeated(lambdas, _), _) =>
        lambdas

      case other =>
        report.errorAndAbort(s"Unknown tree: ${other.show(using Printer.TreeStructure)}")


  @tailrec
  private def tupleTypeToList(tpe: TypeRepr, acc: List[TypeRepr] = Nil): List[TypeRepr] =
    tpe.asType match
      case '[h *: t] =>
        tupleTypeToList(TypeRepr.of[t], acc :+ TypeRepr.of[h])
      case '[EmptyTuple] =>
        acc
      case _ =>
        acc :+ tpe


  private def tupled(args: List[Term]): Term =
    args match
      case Nil => '{()}.asTerm
      case oneArg :: Nil => oneArg
      case args =>
        args.foldRight[Term]('{ EmptyTuple }.asTerm) { (el, acc) =>
          Select
            .unique(acc, "*:")
            .appliedToTypes(List(el.tpe, acc.tpe))
            .appliedToArgs(List(el))
        }

  extension (value: StubWithMethod)
    def show: Expr[String] = '{
      val idx = ${value.selectReflect[Int](_ => UniqueStubIdx)}
      val method = ${Expr(value.method.show)}
      s"<stub-$idx> $method"
    }


  extension (method: MockableDefinition)
    private def callsRef(classSymbol: Symbol): Expr[AtomicReference[List[Any]]] =
      Ref(classSymbol.declaredField(method.callsValName)).asExprOf[AtomicReference[List[Any]]]

    private def functionRef(classSymbol: Symbol): Expr[AtomicReference[Option[Any => Any]]] =
      Ref(classSymbol.declaredField(method.mockValName)).asExprOf[AtomicReference[Option[Any => Any]]]

    private def show: String =
      given Printer[TypeRepr] = Printer.TypeReprShortCode
      s"Stub[${method.ownerTpe.show}].${method.symbol.name}${method.tpe.show}"


  extension (io: Expr[StubIO[?]])
    private def isMatches(resTpe: TypeRepr): Boolean =
      val ioTerm = io.asTerm
      val ioTpe = ioTerm.tpe.select(ioTerm.tpe.typeSymbol.typeMember("Underlying"))
      resTpe <:< AppliedType(ioTpe, List(TypeRepr.of[Any], TypeRepr.of[Any]))

    private def wrap(resTpe: TypeRepr, updateCalls: Expr[Unit], result: Expr[Any]): Term =
      val ioTerm = io.asTerm
      val (errorArgTpe, resArgTpe) = resTpe.dealias.typeArgs match
        case List(_, err, res) => (err, res)
        case List(err, res) => (err, res)
        case List(res) => (TypeRepr.of[Nothing], res)
        case _ => report.errorAndAbort(s"$resTpe is not a type constructor")

      resTpe.asType match
        case '[res] =>
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
                List('{ (_: Unit) => ${ result }.asInstanceOf[res] }.asTerm)
              ),
              "asInstanceOf"
            ),
            List(TypeTree.of[res])
          )