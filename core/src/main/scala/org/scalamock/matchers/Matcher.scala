package org.scalamock.matchers

/**
 * Can be extended to implement custom matchers.
 */
trait Matcher[T] extends MatcherBase {
  override def toString: String = this.getClass.getSimpleName
  override def canEqual(x: Any): Boolean = true // x.isInstanceOf[T] would not work anyway
  override def equals(x: Any) = if (canEqual(x)) safeEquals(x.asInstanceOf[T]) else false

  def safeEquals(that: T): Boolean
}
