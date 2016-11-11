package com.example

import com.example.commands.AddBulletin
import com.example.events.CmdCompleted
import com.example.handlers.AddBulletinHandler
import com.example.messagebus.MessageBus
import kafka.serializer.StringDecoder
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.kafka.KafkaUtils
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.util.Try
import scala.util.control.NonFatal

/**
  * This class represents Streaming Spark Job
  */
abstract class CmdProcessor extends Serializable {

  val topics: Set[String]

  val params: Map[String, String]

  // this hack with ```lazy``` and ```@transient``` allows to load [[addBulletinHandler]]
  // just one time regardless of running kind (restoring from checkpoints or clean start)
  @transient
  val addBulletinHandler: AddBulletinHandler

  @transient
  val messageBus: MessageBus

  /**
    * Synthetic method to catch local variables {{localHandlwer}} and {{messageBus}}
    */
  def runPipeline(ssc: StreamingContext) = {
    val dataStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc,
      params,
      topics)

    dataStream.foreachRDD { rdd =>

      val values = rdd.values

      values.foreach { cmdMessage =>

        implicit val formats = DefaultFormats
        val ast = parse(cmdMessage)

        // take command type and id
        val baseCmd = (ast \ "type").extract[String]
        val cmdId = (ast \ "id").extract[String]

        // dispatch command to corresponding handler
        Try {
          baseCmd match {
            case AddBulletin.TYPE =>
              val cmd = ast.extract[AddBulletin]
              cmd.setId(cmdId)

              addBulletinHandler(cmd)
          }
          CmdCompleted.builder()
            .id(cmdId)
            .succeeded(true)
            .build()

        }.recover {
          case NonFatal(ex) => new CmdCompleted(cmdId, false, ex.getMessage)

        }.foreach { event =>
          messageBus.send(event)

        }
      }
    }
  }
}
