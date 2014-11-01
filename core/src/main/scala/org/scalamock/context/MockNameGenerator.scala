package org.scalamock.context

/** MockNameGenerator is used to create default mock names */
class MockNameGenerator {
  private var mockId: Int = 0

  def generateMockName(prefix: String): Symbol = this.synchronized {
    mockId += 1
    Symbol("%s-%d".format(prefix, mockId))
  }
}
