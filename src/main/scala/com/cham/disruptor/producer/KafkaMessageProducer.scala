package com.cham.disruptor.producer

import java.util.Properties

import akka.actor.Actor
import com.cham.disruptor.app.ValueEventTweet
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

class KafkaMessageProducer {

  val topic = "tweet-publisher-topic"

  val brokers = "localhost:9092"

  val kafkaStringSerializerClass = "org.apache.kafka.common.serialization.StringSerializer"

  val kafkaProps = new Properties()
  kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
  kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaStringSerializerClass)
  kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,kafkaStringSerializerClass)

  val producer = new KafkaProducer[String, String](kafkaProps)

  def publishMessagesToKafka(tweet: ValueEventTweet): Unit ={

    val kafkaTweetMessage = new ProducerRecord[String, String](topic, null, tweet.toString)
    try {
      producer.send(kafkaTweetMessage).get()
    } catch {
      case ex: Exception => printf("Error accessing Kafka..")
    }
  }
}
