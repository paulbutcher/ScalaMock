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

import zio.{IO, UIO, ZIO}

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

    /** Same as [[calls]], but returns ZIO. You still can just use [[calls]] instead. */
    inline def callsZIO[Args <: NonEmptyTuple, R](
      using TupledFunction[F, Args => R]
    ): UIO[List[UntupledOne[Args]]] = ZIO.succeed(f.calls[Args, R])

    /** Same as [[times]], but returns ZIO. You still can just use [[times]] instead. */
    inline def timesZIO: UIO[Int] = ZIO.succeed(f.times)

    /** Same as [[times]] with concrete arguments, but returns ZIO. You still can just use [[times]] instead. */
    inline def timesZIO[Args <: NonEmptyTuple, R](
      using TupledFunction[F, Args => R]
    )(
      args: UntupledOne[Args]
    ): UIO[Int] =
      ZIO.succeed(f.times[Args, R](args))