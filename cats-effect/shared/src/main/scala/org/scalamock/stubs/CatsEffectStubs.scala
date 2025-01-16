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

    
