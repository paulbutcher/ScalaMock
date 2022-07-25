package org.scalamock.scalatest.proxy

import org.scalamock.proxy.ProxyMockFactory
import org.scalamock.scalatest.AbstractAsyncMockFactory
import org.scalatest.AsyncTestSuite

trait AsyncMockFactory extends AbstractAsyncMockFactory with ProxyMockFactory { this: AsyncTestSuite =>

}
