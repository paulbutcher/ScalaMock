package org.scalamock.scalatest.proxy

import org.scalamock.proxy.ProxyMockFactory
import org.scalamock.scalatest.AbstractAsyncMockFactory
import org.scalatest.AsyncTestSuite

/**
  * Created by Luiz Guilherme D'Abruzzo Pereira <luiz290788@gmail.com> on 26/12/16.
  */
trait AsyncMockFactory  extends AbstractAsyncMockFactory with ProxyMockFactory with AsyncTestSuite