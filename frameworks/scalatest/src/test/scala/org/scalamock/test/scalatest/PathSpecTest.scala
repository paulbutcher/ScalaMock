package org.scalamock.test.scalatest

import org.scalamock.scalatest.PathMockFactory
import org.scalatest.exceptions.TestFailedException
import org.scalatest.{Matchers, path}


/**
 * Created: 4/15/15
 */
class PathSpecTest extends path.FunSpec with Matchers with PathMockFactory {
  describe("PathSpec") {
    val mockFn = mockFunction[Int, Int]
    mockFn expects 42

    it("does not throw exception if expectations are met") {
      mockFn(42)

      verifyExpectations()
    }

    it("fails if expectation is not met") {
      an[TestFailedException] should be thrownBy verifyExpectations()
    }
  }
}
