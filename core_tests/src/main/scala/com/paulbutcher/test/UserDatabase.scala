package com.paulbutcher.test

case class User(name: String, age: Int)
case class Address(city: String, street: String)

trait UserDatabase {
  def storeUser(user: User): String
  def addUserAddress(user: User, address: Address): String
}
