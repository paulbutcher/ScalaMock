package org.scalamock.matchers

/**
 * Base trait of all ScalaMock argument matchers.
 *
 * If you want to write a custom matcher please extend the [[Matcher]] trait.
 */
trait MatcherBase extends Equals {
  override def toString: String = "Matcher"
}
