package com.cham.disruptor.consumer

import java.util.{Collections, Properties}
import java.time.{Duration, Instant}

import akka.actor.{Actor, ActorLogging}
import akka.util.Timeout
import com.cham.disruptor.dao.CassandraDaoActorBuilder
import com.cham.disruptor.dto.TweetMessage
import org.apache.kafka.clients.consumer.{CommitFailedException, KafkaConsumer}


class KafkaConsumerActor extends Actor with ActorLogging  {

  val kafkaProps: Properties = new Properties

  kafkaProps.put("bootstrap.servers", "localhost:9092")
  kafkaProps.put("group.id", "group1")
  kafkaProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  kafkaProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  kafkaProps.put("auto.offset.reset", "earliest")
  //kafkaProps.put("enable.auto.commit", "true") // can cause duplicate message consumption in case of rebalance from Kafka
  kafkaProps.put("enable.auto.commit", "false")

  val kafkaConsumer = new KafkaConsumer[String, String](kafkaProps)
  kafkaConsumer.subscribe(Collections.singletonList("tweet-publisher-topic"))

  import com.cham.disruptor.app.MessageConsumerAllApp.system

  val cassandraDaoActor = system.actorOf(CassandraDaoActorBuilder.props(Timeout.zero), CassandraDaoActorBuilder.name)

  def consumeFromDirectKafka(): Unit ={
    println("inside consumeFromDirectKafka" )
    try {
      // infinite loop. App need pooling for messages. Need to close the consumer while testing.
      while (true) {

        val tweetSet = kafkaConsumer.poll(100)

        if (tweetSet.count() !=0) {
          val start = Instant.now
          println("started to push " + tweetSet.count() + " messages ")

          val recordsIterator = tweetSet.iterator()

          while (recordsIterator.hasNext) {
            val tweetRecord = recordsIterator.next().value()
            // TODO : get the record and set to TweetMessage object
            // println(tweetRecord)
            // temp: hard coded to get the write working
            var tweetMessage: TweetMessage = new TweetMessage(1, "I love Scala", "Chaminda W")

            cassandraDaoActor ! tweetMessage

          }

          try {
            kafkaConsumer.commitSync() // commit the ack to Kafka broker
            //kafkaConsumer.commitAsync() // commit to Kafka async - without blocking the thread but can not retry
          } catch {
            case ex: CommitFailedException => {
              printf("Error accessing Kafka..")
              kafkaConsumer.close()
            }
          }

          val end = Instant.now
          val timeElapsed = Duration.between(start, end)
          println("Time taken to push " + tweetSet.count() + " tweets is " + timeElapsed.toMillis + " milliseconds")
        }
        else {
          //println("No more tweets to consume..")
        }
      }
    }finally {
      kafkaConsumer.close()
    }
  }

  def getRecordCount(){
    println("Inside getRecordCount ")
    cassandraDaoActor ! "get"
  }

  def deleteAllRecords(): Unit ={
    println("Inside deleteAllRecords ")
    cassandraDaoActor ! "delete"
  }

  override def receive : Actor.Receive = {
    case "consume" => consumeFromDirectKafka
    case "delete" => deleteAllRecords
    case "get" => getRecordCount
  }

  override def finalize(): Unit = super.finalize()

}
