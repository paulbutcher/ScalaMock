package com.borachio.examples.mocksarentstubs

import org.scalatest.WordSpec
import com.borachio.MockFactory

class OrderTestWithProxies extends WordSpec with MockFactory {
  
  "An order" when {
    "in stock" should {
      "remove inventory" in {
        val mockWarehouse = mock[Warehouse]
        inSequence {
          mockWarehouse expects 'hasInventory withArguments ("Talisker", 50) returning true once;
          mockWarehouse expects 'remove withArguments ("Talisker", 50) once
        }
        
        val order = new Order("Talisker", 50)
        order.fill(mockWarehouse)
        
        assert(order.isFilled)
      }
    }
    
    "out of stock" should {
      "remove nothing" in {
        val mockWarehouse = mock[Warehouse]
        mockWarehouse expects 'hasInventory returns false once
        
        val order = new Order("Talisker", 50)
        order.fill(mockWarehouse)
        
        assert(!order.isFilled)
      }
    }
  }
}
