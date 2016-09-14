package com.example

import java.util
import java.util.Properties

import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}


object Main2 {

  def main (args: Array[String]) {

    val props = new Properties()


    val prod = new KafkaProducer[String, String](producerConfigs)
    prod.send(new ProducerRecord("custom_events", "string"))

    val consumer = new KafkaConsumer[String, String](consumerConfigs())
    consumer.subscribe(util.Arrays.asList("custom_events"))
    val data2 = consumer.poll(1000)
  }

  def consumerConfigs(): java.util.Map[String, AnyRef] = {
    val props: java.util.Map[String, AnyRef] = new java.util.HashMap[String, AnyRef]
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, boolean2Boolean(true))
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "groupId")
    props
  }
  def producerConfigs(): java.util.Map[String, AnyRef] = {
    val props: java.util.Map[String, AnyRef] = new java.util.HashMap[String, AnyRef]
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
    props

  }
}
