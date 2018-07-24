package org.scalamock.matchers


object ArgCapture {
  trait Capture[T] {
    def value: T
    def value_=(t: T): Unit
  }

  case class CaptureOne[T]() extends Capture[T] {
    private var v: Option[T] = None
    override def value_=(t: T): Unit = this.synchronized { v = Option(t) }
    def value: T = v.get
  }

  case class CaptureAll[T]() extends Capture[T] {
    private var v: Seq[T] = Nil
    override def value_=(t: T): Unit = this.synchronized { v :+= t }
    def values: Seq[T] = v
    def value: T = v.last
  }

  class CaptureMatcher[T](c: Capture[T]) extends MatchAny {
    override def equals(that: Any): Boolean = {
      c.value = that.asInstanceOf[T]
      super.equals(that)
    }
  }

}
