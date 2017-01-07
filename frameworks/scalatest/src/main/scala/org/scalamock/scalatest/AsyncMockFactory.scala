package org.scalamock.scalatest

import org.scalamock.clazz.Mock
import org.scalatest.AsyncTestSuite

/**
  * Created by Luiz Guilherme D'Abruzzo Pereira <luiz290788@gmail.com> on 22/12/16.
  */
trait AsyncMockFactory extends AbstractAsyncMockFactory with Mock with AsyncTestSuite