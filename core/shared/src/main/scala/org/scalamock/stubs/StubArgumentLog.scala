package org.scalamock.stubs

/* Allows to override string argument representation in call log. */
trait StubArgumentLog[A]:
  def log(a: A): String


object StubArgumentLog:
  given [A]: StubArgumentLog[A] = (a: A) => a.toString