package com.borachio.examples

trait Warehouse {
  def hasInventory(product: String, quantity: Int): Boolean
  def remove(product: String, quantity: Int)
}
