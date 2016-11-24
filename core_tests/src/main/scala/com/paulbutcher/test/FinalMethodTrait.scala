package com.paulbutcher.test


trait FinalMethodTrait {

  def somePublicMethod(param: String)

  final def someFinalMethod(param: Int) = "final method"

}
