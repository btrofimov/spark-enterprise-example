package com.example.domain

import com.mongodb._
import org.bson.Document

/**
  * Quick and ugly DAO implementation over Mongo
  */
abstract class BulletinMongoRepository extends AbstractRepository[Bulletin] {

  import BulletinMongoRepository._

  val collectionPath: String

  val mongoClient: MongoClient

  override def add(obj: Bulletin): Unit = {
    val (database, collection) = getDatabase(collectionPath)
    collection.insertOne(obj.toDocument())
  }

  private def getDatabase(collectionPath: String) = {
    val Array(databaseName, collectionName) = collectionPath.split("\\.")
    val database = this.mongoClient.getDatabase(databaseName)
    val collection = database.getCollection(collectionName)
    (database, collection)
  }
}

object BulletinMongoRepository {

  val ID      = "_id"
  val DATE    = "date"
  val AUTHOR  = "author"
  val MESSAGE = "message"


  implicit class Convertor(row: Bulletin) {
    def toDocument(): Document = {
      val fields = new Document
      fields.append(ID, row.id)
      fields.append(DATE, row.date.toString)
      fields.append(AUTHOR, row.author)
      fields.append(MESSAGE, row.message)
      fields
    }
  }

  implicit class Convertor2(doc: Document) {
    def toMongoState(): Bulletin = {
      Bulletin(
        doc.get(ID).asInstanceOf[String],
        doc.get(DATE).asInstanceOf[String],
        doc.get(AUTHOR).asInstanceOf[String],
        doc.get(MESSAGE).asInstanceOf[String]
      )
    }
  }
}
