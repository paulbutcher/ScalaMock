package com.paulbutcher.smock.example

class Order(product: String, quantity: Int) {
  
  def fill(warehouse: Warehouse) {
    if (warehouse.hasInventory(product, quantity)) {
      warehouse.remove(product, quantity)
      filled = true
    }
  }
  
  def isFilled = filled
  
  private var filled = false
}
