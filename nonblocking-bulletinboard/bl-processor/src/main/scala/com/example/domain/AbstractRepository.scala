package com.example.domain

/**
  * Abstract DAO contract
  * @tparam T
  */
trait AbstractRepository[T] {

  def add(obj: T): Unit

}
