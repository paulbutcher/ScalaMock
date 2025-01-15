package org.scalamock.stubs.internal

import org.scalamock.stubs.{Stub, internal}

import java.util.concurrent.atomic.AtomicReference

private[stubs] class CreatedStubs:
  private val stubs: AtomicReference[List[Stub[Any]]] = new AtomicReference(Nil)

  def bind[T](stub: Stub[T]): Stub[T] =
    stubs.updateAndGet(stub :: _)
    stub

  def clearAll(): Unit =
    stubs.updateAndGet { stubs =>
      stubs.foreach:
        _
          .asInstanceOf[scala.reflect.Selectable]
          .applyDynamic(internal.ClearStubsMethodName)()
      stubs
    }
