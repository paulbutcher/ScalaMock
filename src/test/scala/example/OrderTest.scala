package com.paulbutcher.smock.example

import org.scalatest.WordSpec
import com.borachio.MockFactory

class OrderTest extends WordSpec with MockFactory {
  
  val hasInventoryMock = mockFunction[String, Int, Boolean]
  val removeMock = mockFunction[String, Int, Unit]

  val mockWarehouse = new Warehouse {
    def hasInventory(product: String, quantity: Int) = hasInventoryMock(product, quantity)
    def remove(product: String, quantity: Int) = removeMock(product, quantity)
  }
  
  "An order" when {
    "in stock" should {
      "remove inventory" in {
        inSequence {
          hasInventoryMock expects ("Talisker", 50) returning true once;
          removeMock expects ("Talisker", 50) once
        }
        
        val order = new Order("Talisker", 50)
        order.fill(mockWarehouse)
        
        assert(order.isFilled)
      }
    }
    
    "out of stock" should {
      "remove nothing" in {
        hasInventoryMock returns false once
        
        val order = new Order("Talisker", 50)
        order.fill(mockWarehouse)
        
        assert(!order.isFilled)
      }
    }
  }
}
