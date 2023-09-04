// Copyright (c) 2011-2015 ScalaMock Contributors (https://github.com/paulbutcher/ScalaMock/graphs/contributors)
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

package com.example

import org.scalamock.specs2.MockContext
import org.specs2.mutable.Specification

/**
 * This is a demonstration and test of the Specs2 integration, using the example from
 * Martin Fowler's article Mocks Aren't Stubs http://martinfowler.com/articles/mocksArentStubs.html
 */
class OrderSpecification extends Specification {

  "An order" should {
    "remove inventory when in stock" in new MockContext {
      val mockWarehouse = mock[Warehouse]
      inSequence {
        (mockWarehouse.hasInventory _).expects("Talisker", 50).returning(true).once()
        (mockWarehouse.remove _).expects("Talisker", 50).once()
      }
      val order = new Order("Talisker", 50)
      order.fill(mockWarehouse)
      order.isFilled must beTrue
    }

    "remove nothing when out of stock" in new MockContext {
      val mockWarehouse = mock[Warehouse]
      (mockWarehouse.hasInventory _).expects(*, *).returns(false).once()
      val order = new Order("Talisker", 50)
      order.fill(mockWarehouse)
      order.isFilled must beFalse
    }
  }
}