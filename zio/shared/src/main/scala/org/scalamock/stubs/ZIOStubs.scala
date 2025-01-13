package org.scalamock.stubs

import zio.{UIO, ZIO, IO}

import scala.util.{NotGiven, TupledFunction}

trait ZIOStubs extends Stubs:
  final given StubIO[IO] = new StubIO[IO]:
    def die(ex: Throwable): UIO[Nothing] =
      ZIO.die(ex)
      
    def succeed[T](t: => T): UIO[T] =
      ZIO.succeed(t)

    def flatMap[E, EE >: E, T, T2](fa: IO[E, T])(f: T => IO[EE, T2]): IO[EE, T2] =
      fa.flatMap(f)


  extension [F](inline f: F)
    /** Same as [[returns]] without arguments, but returns ZIO */
    inline def returnsZIO[E, A](
      using NotGiven[TupledFunction[F, ?]],
    )(
      value: IO[E, A]
    )(using IO[E, A] <:< F): UIO[Unit] = ZIO.succeed(f.returns(value))

    /** Same as [[returns]] with arguments, but returns ZIO */
    inline def returnsZIO[Args <: NonEmptyTuple, E, R](using
      TupledFunction[F, Args => IO[E, R]]
    )(
      value: UntupledOne[Args] => IO[E, R]
    ): UIO[Unit] = ZIO.succeed(f.returns[Args, IO[E, R]](value))

    /** Same as [[calls]], but returns ZIO */
    inline def callsZIO[Args <: NonEmptyTuple, R](
      using TupledFunction[F, Args => R]
    ): UIO[List[UntupledOne[Args]]] = ZIO.succeed(f.calls[Args, R])

    /** Same as [[times]], but returns ZIO */
    inline def timesZIO: UIO[Int] = ZIO.succeed(f.times)

    /** Same as [[times]] with concrete arguments, but returns ZIO */
    inline def timesZIO[Args <: NonEmptyTuple, R](
      using TupledFunction[F, Args => R]
    )(
      args: UntupledOne[Args]
    ): UIO[Int] =
      ZIO.succeed(f.times[Args, R](args))