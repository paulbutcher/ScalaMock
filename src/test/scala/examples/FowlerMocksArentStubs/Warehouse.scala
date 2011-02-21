package com.borachio.examples.mocksarentstubs

trait Warehouse {
  def hasInventory(product: String, quantity: Int): Boolean
  def remove(product: String, quantity: Int)
}
