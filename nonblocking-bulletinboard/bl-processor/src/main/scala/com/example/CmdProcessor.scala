package com.example

import com.example.commands.AddBulletin
import com.example.handlers.AddBulletinHandler
import kafka.serializer.StringDecoder
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.kafka.KafkaUtils
import org.json4s._
import org.json4s.jackson.JsonMethods._

/**
  *
  */
abstract class CmdProcessor {

  val topics: Set[String]

  val params: Map[String, String]

  def addBulletinHandlerFactory: () => AddBulletinHandler

  /**
    * Synthetic method to catch local variables {{localHandlwer}} and {{messageBus}}
    */
  def runPipeline(ssc: StreamingContext) = {
    val dataStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc,
      params,
      topics)

    val localHandler = addBulletinHandlerFactory

    dataStream.foreachRDD { rdd =>

      val values = rdd.values

      values.foreach { cmdMessage =>

        implicit val formats = DefaultFormats
        val ast = parse(cmdMessage)

        // take command type and id
        val baseCmd = (ast \ "type").extract[String]
        val cmdId = (ast \ "id").extract[String]

        // dispatch command to corresponding handler
        baseCmd match {
          case AddBulletin.TYPE =>
            val cmd = ast.extract[AddBulletin]
            cmd.setId(cmdId)

            localHandler()(cmd)
        }
      }
    }
  }
}
