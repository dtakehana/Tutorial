package models

/**
 * Created by d-takehana on 2015/09/04.
 */
case class Person(age: Int, name: Name, bloodType: Option[String], numbers: Seq[Int])
case class Name(first: String, last: String)
case class Cart(no: Long, goods: String)
case class PersonCarts(person: Person, cart: Seq[Cart])