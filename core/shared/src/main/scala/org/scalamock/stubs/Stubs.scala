// Copyright (c) 2011-2025 ScalaMock Contributors (https://github.com/ScalaMock/ScalaMock/graphs/contributors)
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

package org.scalamock.stubs

import java.util.concurrent.atomic.AtomicReference
import scala.quoted.{Expr, Quotes, Type}
import scala.util.{NotGiven, TupledFunction}

/** Indicates that object of type T was generated */
opaque type Stub[+T] <: T = T

trait Stubs:
  /** Collects all generated stubs */
  final given stubs: internal.CreatedStubs = internal.CreatedStubs()

  /**
   *  Resets all recorded stub functions and arguments.
   *  This is useful if you want to create your stub once per suite.
   *  Note that in such case test cases should run sequentially
   */
  final def resetStubs(): Unit = stubs.clearAll()

  /** Generates an object of type T with possibility to record methods arguments and set-up method results */
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
     *   foo.foo2.calls // List((0, 0), (1, 1))
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
     *   foo.foo1.times(3) // 2
     *   foo.foo2.times((1, 1)) // 1
     * }}}
     */
    inline def times[Args <: NonEmptyTuple, R](
      using TupledFunction[F, Args => R]
    )(
      args: UntupledOne[Args]
    ): Int =
      callsImpl[F, Args, R](f).count(_ == args)

    inline def isBefore(inline other: Any)(using log: CallLog): Boolean =
      val List(fString, gString) = parseMethodsImpl(f, other)
      val actual = log.internal.calledMethods
      actual.indexOf(gString, actual.indexOf(fString)) != -1

    inline def isAfter(inline other: Any)(using log: CallLog): Boolean =
      val List(fString, gString) = parseMethodsImpl(f, other)
      val actual = log.internal.calledMethods
      actual.indexOf(fString, actual.indexOf(gString)) != -1

  class CallLog:
    override def toString: String = internal.calledMethods.mkString("\n")

    object internal:
      private val methodsRef: AtomicReference[List[String]] = AtomicReference(Nil)
      private val uniqueIdx: AtomicReference[Int] = AtomicReference(0)
      def nextIdx: Int = uniqueIdx.updateAndGet(_ + 1)
      def write(methodName: String): Unit = { methodsRef.getAndUpdate(methodName :: _); () }
      def clear(): Unit =
        methodsRef.set(Nil)
        uniqueIdx.set(0)
      def calledMethods: List[String] = internal.methodsRef.get().reverse



private
inline def stubImpl[T](using collector: internal.CreatedStubs): Stub[T] =
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
inline def parseMethodsImpl(inline calls: Any*): List[String] =
  ${ parseMethodsMacro('{ calls }) }

private
def stubMacro[T: Type](collector: Expr[internal.CreatedStubs])(using Quotes): Expr[Stub[T]] =
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

private
def parseMethodsMacro(calls: Expr[Seq[Any]])(using quotes: Quotes): Expr[List[String]] =
  new internal.StubMaker().parseMethods(calls)

private[stubs] type UntupledOne[X <: NonEmptyTuple] = X match
  case head *: EmptyTuple => head
  case _ => X
