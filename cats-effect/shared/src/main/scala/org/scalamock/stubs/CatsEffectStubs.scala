package org.scalamock.stubs

import cats.effect.IO

import scala.util.{NotGiven, TupledFunction}

trait CatsEffectStubs extends Stubs:
  final given StubIO[[x, y] =>> IO[y]] = new StubIO[[x, y] =>> IO[y]]:
    def succeed[T](t: => T): IO[T] =
      IO(t)

    def flatMap[E, EE >: E, T, T2](fa: IO[T])(f: T => IO[T2]): IO[T2] =
      fa.flatMap(f)

    def die(ex: Throwable): IO[Nothing] = IO.raiseError(ex)


  extension [F](inline f: F)

    /** Same as [[returns]] without arguments, but returns IO */
    inline def returnsIO[E, A](
      using NotGiven[TupledFunction[F, ?]],
    )(
      value: IO[A]
    )(using IO[A] <:< F): IO[Unit] = IO(f.returns(value))

    /** Same as [[returns]] with arguments, but returns IO */
    inline def returnsIO[Args <: NonEmptyTuple, R](
      using TupledFunction[F, Args => IO[R]]
    )(
      value: UntupledOne[Args] => IO[R]
    ): IO[Unit] = IO(f.returns[Args, IO[R]](value))

    /** Same as [[calls]], but returns IO. You still can just use [[calls]] instead. */
    inline def callsIO[Args <: NonEmptyTuple, R](
      using TupledFunction[F, Args => R]
    ): IO[List[UntupledOne[Args]]] = IO(f.calls[Args, R])

    /** Same as [[times]], but returns IO. You still can just use [[times]] instead. */
    inline def timesIO: IO[Int] = IO(f.times)

    /** Same as [[times]] with concrete arguments, but returns IO. You still can just use [[times]] instead. */
    inline def timesIO[Args <: NonEmptyTuple, R](
      using TupledFunction[F, Args => R]
    )(
      args: UntupledOne[Args]
    ): IO[Int] =
      IO(f.times[Args, R](args))

    
