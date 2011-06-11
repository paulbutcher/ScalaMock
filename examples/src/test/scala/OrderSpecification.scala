package com.borachio.examples

import org.specs2.mutable.Specification
import com.borachio.specs2.MockFactory

/**
 * This is a demonstration and test of the Specs2 integration, using the example from
 * Martin Fowler's article Mocks Aren't Stubs http://martinfowler.com/articles/mocksArentStubs.html
 */
class OrderSpecification extends Specification with MockFactory {
  val hasInventoryMock = mockFunction[String, Int, Boolean]
  val removeMock = mockFunction[String, Int, Unit]

  val mockWarehouse = new Warehouse {
    def hasInventory(product: String, quantity: Int) = hasInventoryMock(product, quantity)
    def remove(product: String, quantity: Int) = removeMock(product, quantity)
  }

  "An order" should  {
    "remove inventory when in stock" in {
      inSequence {
        hasInventoryMock expects ("Talisker", 50) returning true once;
        removeMock expects ("Talisker", 50) once
      }
      val order = new Order("Talisker", 50)
      order.fill(mockWarehouse)
      order.isFilled must beTrue
    }

    "remove nothing when out of stock" in {
      hasInventoryMock expects (*, *) returns false once
      val order = new Order("Talisker", 50)
      order.fill(mockWarehouse)
      order.isFilled must beFalse
    }
  }
}
