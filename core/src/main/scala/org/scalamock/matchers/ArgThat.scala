package org.scalamock.matchers

/** Matcher that uses provided predicate to perform matching */
class ArgThat[T](predicate: T => Boolean) extends Matcher[T] {
  override def toString: String = "<matcher>"
  override def safeEquals(obj: T): Boolean = predicate(obj)
}
