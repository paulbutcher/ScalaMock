package com.paulbutcher.smock.example

trait Warehouse {
  def hasInventory(product: String, quantity: Int): Boolean
  def remove(product: String, quantity: Int)
}
