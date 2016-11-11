package com.example

import java.util.Date

import com.example.domain.BulletinMongoRepository
import com.example.handlers.AddBulletinHandler
import com.example.messagebus.MessageBus
import com.example.infrastructure.Utils._
import com.mongodb.MongoClient
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.StreamingContext
import org.springframework.kafka.core.{DefaultKafkaProducerFactory, KafkaTemplate}

import scala.concurrent.duration._

/**
  * Major application context where all beans are linked to each other
  */
object AppContext extends ConfigTrait {

  //  set up local mode
  lazy val sparkConf = new SparkConf().
    setMaster("local[*]").
    set("spark.ui.enabled", "false").
    set("spark.app.id", new Date().toString).
    setAppName("command-processor")

  lazy val kafkaContext = topics -> Map("metadata.broker.list" -> brokers)

  lazy val _mongoClient = new MongoClient(mongoHost)

  lazy val _repository = new BulletinMongoRepository {
    override lazy val collectionPath = _collectionPath
    override lazy val mongoClient = _mongoClient
  }

  lazy val _addBulletinHandler = new AddBulletinHandler {
    override val repository = _repository
  }

  lazy val kafkaProducer = {
    val props = new java.util.HashMap[String, AnyRef]
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
    val producerFactory = new DefaultKafkaProducerFactory[String, String](props)
    new KafkaTemplate[String, String](producerFactory)
  }

  lazy val _messageBus = new MessageBus {
    producer = kafkaProducer
    eventsTopic = _eventsTopic
  }

  lazy val cmdProcessor = new CmdProcessor {

    override lazy val topics = kafkaContext._1
    override lazy val params = kafkaContext._2

    override lazy val addBulletinHandler = _addBulletinHandler

    override lazy val messageBus = _messageBus
  }

  def buildNewStreamingContext(): () => StreamingContext = () => {
    val ssc = new StreamingContext(sparkConf, 100.millis)
    AppContext.cmdProcessor.runPipeline(ssc)
    ssc.checkpoint(checkpointDirectory)
    ssc
  }

  lazy val _ssc: StreamingContext = {
    StreamingContext.getOrCreate(checkpointDirectory, buildNewStreamingContext())
  }

  class MainApp extends App {
    _ssc.start()
    _ssc.awaitTermination()
  }
}

/**
  * Major entry point for the application
  */
object MainApp extends AppContext.MainApp