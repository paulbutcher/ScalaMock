package com.borachio

trait Mock {
  def expects(name: Symbol): Expectation
}
