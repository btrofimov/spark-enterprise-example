package com.example.handlers


object AbstractHandler {

  type Handler[T] = T => Unit

}
