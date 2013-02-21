package com.globalmaksimum.appservices

trait IDEntity[T] {
  def id: T
}
case class Organization(val id: java.lang.Long, val name: String, var users: Seq[User]) extends IDEntity[java.lang.Long]

case class Application(val id: java.lang.Long) extends IDEntity[java.lang.Long]

case class Resource(val id: java.lang.Long, val name: String) extends IDEntity[java.lang.Long]

case class User(val id: java.lang.Long, val name: String, val surname: String, val username: String) extends IDEntity[java.lang.Long]