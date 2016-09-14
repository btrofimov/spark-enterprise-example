package com.example

import com.typesafe.config.{Config, ConfigFactory}
import scala.collection.JavaConverters._


trait ConfigTrait {

  lazy val mainConfig: Config = ConfigFactory.load()

  val checkpointDirectory = mainConfig.getString("app.kafka.checkpoint-dir")
  val _collectionPath = mainConfig.getString("app.mongo.collection-path")
  val mongoHost = mainConfig.getString("app.mongo.host")
  val brokers = mainConfig.getString("app.kafka.brokers")
  val topics = mainConfig.getStringList("app.kafka.commands-topics").asScala.toSet
  val _eventsTopic = mainConfig.getString("app.kafka.events-topic")
}
