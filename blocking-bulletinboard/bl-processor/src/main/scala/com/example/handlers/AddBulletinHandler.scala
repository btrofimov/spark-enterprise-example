package com.example.handlers

import java.time.{LocalDateTime, ZoneId}

import AbstractHandler._
import com.example.commands.AddBulletin
import com.example.domain.{AbstractRepository, Bulletin}


abstract class AddBulletinHandler extends Handler[AddBulletin] {

  val repository: AbstractRepository[Bulletin]

  override def apply(cmd: AddBulletin): Unit = {

    val entity = Bulletin(
      cmd.getId,
      LocalDateTime.now(ZoneId.systemDefault).toString(),
      cmd.getAuthor,
      cmd.getMessage)

    repository.add(entity)
  }
}
