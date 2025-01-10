package org.scalamock.stubs

import org.scalamock.stubs.internal.ArgExpectation

import java.util.concurrent.atomic.AtomicReference
import scala.quoted.{Expr, Quotes, Type}
import scala.util.{NotGiven, TupledFunction}

/** Indicates that object of type [[T]] was generated */
opaque type Stub[+T] <: T = T

trait Stubs:
  /** Collects all generated stubs */
  final given stubs: Created = Created()

  /**
   *  Resets all recorded stub functions and arguments.
   *  This is useful if you want to create your stub once per suite.
   *  Note that in such case test cases should run sequentially
   */
  final def resetStubs(): Unit = stubs.clearAll()

  /** Generates an object of type [[T]] with possibility to record methods arguments and set-up method results */
  inline def stub[T]: Stub[T] = stubImpl[T]

  extension [F](inline f: F)

    /** Allows to set result for method without arguments.
     *  {{{
     *   trait Foo:
     *     def foo0: Int
     *
     *   val foo = stub[Foo]
     *
     *   foo.foo0.returns(5)
     *   foo.foo0 // 5
     *  }}}
     * */
    inline def returns(
      using NotGiven[TupledFunction[F, ?]]
    )(
      value: F
    ): Unit = returnsImpl[F](f, value)

    /**
     *  Allows to set result for method with arguments.
     *  {{{
     *   trait Foo:
     *     def foo1(x: Int): Int
     *     def foo2(x: Int, y: Int): Int
     *
     *   val foo = stub[Foo]
     *
     *   foo.foo1.returns:
     *     case 1 => 2
     *     case 2 => 5
     *     case _ => 0
     *
     *   foo.foo2.returns:
     *     case (0, 0) => 1
     *     case _ => 0
     *
     *   foo.foo1(2) // 5
     *   foo.foo2(0, 0) // 1
     *  }}}
     */
    inline def returns[Args <: NonEmptyTuple, R](
      using TupledFunction[F, Args => R]
    )(
      value: UntupledOne[Args] => R
    ): Unit = returnsImpl[F, Args, R](f, value)

    /**
     * Allows to get caught method arguments.
     * For multiple arguments - returns them tupled.
     * One list item per call.
     * {{{
     *   trait Foo:
     *     def foo1(x: Int): Int
     *     def foo2(x: Int, y: Int): Int
     *
     *   val foo = stub[Foo]
     *
     *   foo.foo1.returns(_ => 1)
     *   foo.foo2.returns(_ => 1)
     *
     *   foo.foo1(2)
     *   foo.foo1(2)
     *   foo.foo2(0, 0)
     *   foo.foo2(1, 1)
     *
     *   foo.foo1.calls // List(2, 2)
     *   foo.foo2.calls // List((0, 0))
     * }}}
     */
    inline def calls[Args <: NonEmptyTuple, R](
      using TupledFunction[F, Args => R]
    ): List[UntupledOne[Args]] = callsImpl[F, Args, R](f)

    /**
     * Allows to get number of times method was executed.
     * {{{
     *   trait Foo:
     *     def foo1(x: Int): Int
     *
     *   val foo = stub[Foo]
     *   foo.foo1.returns(_ => 1)
     *
     *   foo.foo1(2)
     *   foo.foo1(3)
     *
     *   foo.foo1.times // 2
     * }}}
     */
    inline def times: Int =
      scala.compiletime.summonFrom {
        case given NotGiven[TupledFunction[F, _]] =>
          timesImpl[F, EmptyTuple, F](f)

        case tf: TupledFunction[F, args => r] =>
          timesImpl[F, args, r](f)
      }

    /**
     * Allows to get number of times method was executed with specified arguments.
     * Multiple arguments should be provided as tuple.
     * {{{
     *   trait Foo:
     *     def foo1(x: Int): Int
     *     def foo2(x: Int, y: Int): Int
     *
     *   val foo = stub[Foo]
     *   foo.foo1.returns(_ => 1)
     *   foo.foo2.returns(_ => 2)
     *
     *   foo.foo1(3)
     *   foo.foo1(3)
     *   foo.foo2(1, 1)
     *
     *   foo.foo1.times(1) // 0
     *   foo.foo1.times(2) // 2
     *   foo.foo2.times((1, 1)) // 1
     * }}}
     */
    inline def times[Args <: NonEmptyTuple, R](
      using TupledFunction[F, Args => R]
    )(
      args: UntupledOne[Args]
    ): Int =
      callsImpl[F, Args, R](f).count(_ == args)
    
    inline def expectedWith[Args <: NonEmptyTuple, R](
      using TupledFunction[F, Args => R]
    )(
      args: UntupledOne[Args]
    )(using log: CallLog): ArgExpectation = new ArgExpectation:
      protected def actualTimes: Int =
        callsImpl[F, Args, R](f).count(_ == args)

    inline def expectedWhere[Args <: NonEmptyTuple, R](
      using TupledFunction[F, Args => R]
    )(
      args: UntupledOne[Args] => Boolean
    )(using log: CallLog): ArgExpectation = new ArgExpectation:
      protected def actualTimes: Int =
        callsImpl[F, Args, R](f).count(args)
    

  private[stubs] class Created:
    private val stubs: AtomicReference[List[Stub[Any]]] = new AtomicReference(Nil)

    def bind[T](stub: Stub[T]): Stub[T] =
      stubs.updateAndGet(stub :: _)
      stub

    def clearAll(): Unit =
      stubs.updateAndGet { stubs =>
        stubs.foreach:
          _
            .asInstanceOf[scala.reflect.Selectable]
            .applyDynamic(internal.ClearStubsMethodName)()
        stubs
      }

  /* This API is not stable enough, may be changed in the future */
  class CallLog:
    private[CallLog] case class InternalCall(methodName: String, args: List[List[(Any, StubArgumentLog[Any])]])
    case class Call(methodName: String, args: List[List[Any]])
    private val callsRef: AtomicReference[List[InternalCall]] = new AtomicReference[List[InternalCall]](Nil)
    // TODO expectationsRef
    
    def write(methodName: String, args: List[List[(Any, StubArgumentLog[Any])]]) =
      callsRef.getAndUpdate(calls => InternalCall(methodName, args) :: calls)

    //TODO def verify()
    
    def methods: List[String] = callsRef.get().map(_.methodName)
    def calls: List[Call] = callsRef.get().map { call => Call(call.methodName, call.args.map(_.map(_._1))) }

    def full: String =
      callsRef.get().reverse.map { call =>
        s"""
            |Method: ${call.methodName}${
                call.args.zipWithIndex.map { (list, idx) =>
                  s"""
                     |Argument list $idx:
                     |${
                    list.zipWithIndex
                      .map { case ((arg, writer), idx) => s"$idx: ${writer.log(arg)}" }
                      .mkString("\n")
                  }
                     |""".stripMargin
                }.mkString("\n")
              }
            |----------------------
            |""".stripMargin
        }.mkString("")


private
inline def stubImpl[T](using collector: Stubs#Created): Stub[T] =
  ${ stubMacro[T]('{ collector }) }

private
inline def returnsImpl[F](inline f: F, inline value: F): Unit =
  ${ returnsMacro[F]('{ f }, '{ value }) }

private
inline def returnsImpl[F, Args <: NonEmptyTuple, R](
  inline f: F,
  inline value: UntupledOne[Args] => R
): Unit =
    ${ returnsMacro[F, UntupledOne[Args], R]('{ f }, '{ value }) }

private 
inline def callsImpl[F, Args <: NonEmptyTuple, R](inline f: F): List[UntupledOne[Args]] =
  ${ callsMacro[F, UntupledOne[Args], R]('{ f }) }

private
inline def timesImpl[F, Args, R](inline f: F): Int =
  ${ timesMacro[F, Args, R]('{ f }) }

private
def stubMacro[T: Type](collector: Expr[Stubs#Created])(using Quotes): Expr[Stub[T]] =
  new internal.StubMaker().newInstance[T](collector)

private
def returnsMacro[F: Type, Args: Type, R: Type](f: Expr[F], fun: Expr[Args => R])(using quotes: Quotes): Expr[Unit] =
  new internal.StubMaker().returnsMacro[F, Args, R](f, fun)

private
def returnsMacro[F: Type](fun: Expr[F], value: Expr[F])(using quotes: Quotes): Expr[Unit] =
  new internal.StubMaker().returnsMacro[F](fun, value)

private
def callsMacro[F: Type, Args: Type, R: Type](f: Expr[F])(using quotes: Quotes): Expr[List[Args]] =
  new internal.StubMaker().callsMacro[F, Args, R](f)

private
def timesMacro[F: Type, Args: Type, R: Type](f: Expr[F])(using quotes: Quotes): Expr[Int] =
  '{ ${ new internal.StubMaker().callsMacro[F, Args, R](f) }.length }

private[stubs] type UntupledOne[X <: NonEmptyTuple] = X match
  case head *: EmptyTuple => head
  case _ => X
