package org.scalamock.stubs

/** Allows to integrate functional effects */
private[stubs]
trait StubIO[F[+_, +_]]:
  type Underlying[+A, +B] = F[A, B]
  
  def die(ex: Throwable): F[Nothing, Nothing]
  def succeed[T](t: => T): F[Nothing, T]
  def flatMap[E, EE >: E, T, T2](fa: F[E, T])(f: T => F[EE, T2]): F[EE, T2]