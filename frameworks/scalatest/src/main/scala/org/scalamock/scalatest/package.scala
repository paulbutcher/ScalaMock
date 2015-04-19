package org.scalamock

import org.scalatest.exceptions.StackDepthException

/**
 * Created: 4/15/15
 */
package object scalatest {
  private[scalatest] def failedCodeStackDepthFn(methodName: Option[Symbol]): StackDepthException => Int = e => {
    e.getStackTrace indexWhere { s =>
      !s.getClassName.startsWith("org.scalamock") && !s.getClassName.startsWith("org.scalatest") &&
          !(s.getMethodName == "newExpectationException") && !(s.getMethodName == "reportUnexpectedCall") &&
          !(methodName.isDefined && s.getMethodName == methodName.get.name)
    }
  }
}
