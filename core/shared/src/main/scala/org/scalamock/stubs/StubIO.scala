package org.scalamock.stubs

/** Allows to integrate functional effects */
private[stubs]
trait StubIO[F[+_, +_]]:
  type Underlying[+A, +B] = F[A, B]
  
  def succeed[T](t: => T): F[Nothing, T]
  def flatMap[E, EE >: E, T, T2](fa: F[E, T])(f: T => F[EE, T2]): F[EE, T2]

private[stubs]
object StubIO:
  trait Mono[F[+_]]:
    def succeed[T](t: => T): F[T]
    def flatMap[E, EE >: E, T, T2](fa: F[T])(f: T => F[T2]): F[T2]
