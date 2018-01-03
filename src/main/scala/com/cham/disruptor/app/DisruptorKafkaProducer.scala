package com.cham.disruptor.app

/**
  * Created by cwijayasundara on 08/12/2017.
  */

import java.time.{Duration, Instant}
import java.util.concurrent.Executors

import com.cham.disruptor.producer.KafkaMessageProducer
import com.google.gson.Gson
import com.lmax.disruptor._
import com.lmax.disruptor.dsl.{Disruptor, ProducerType}


case class ValueEventTweet(var id: Long, var tweet: String, var user: String){
  override def toString: String = {
    val gson = new Gson
    val jsonString = gson.toJson(this)
    jsonString
  }
}

case class ValueEventTweetTranslator(inTweet: ValueEventTweet) extends EventTranslator[ValueEventTweet]{
  def translateTo(event: ValueEventTweet, sequence: Long) = {
    event.id = inTweet.id
    event.tweet = inTweet.tweet
    event.user = inTweet.user
  }
}

class ValueEventTweetHandler() extends EventHandler[ValueEventTweet] {

  val kafkaProducer: KafkaMessageProducer = new KafkaMessageProducer

  override def onEvent(event: ValueEventTweet, sequence: Long, endOfBatch: Boolean): Unit = {
    kafkaProducer.publishMessagesToKafka(event)
  }
}

object DisruptorExecutor extends App {

    val ring_size: Int = 1024 * 16
    val executor = Executors.newFixedThreadPool(10)

    val blockingWaitStrategy:BlockingWaitStrategy = new BlockingWaitStrategy

    val factory = new EventFactory[ValueEventTweet] {
      override def newInstance(): ValueEventTweet = ValueEventTweet(0L,"0","0")
    }

    val handler = new ValueEventTweetHandler

    val disruptor = new Disruptor[ValueEventTweet](factory,ring_size,executor,ProducerType.MULTI,blockingWaitStrategy)

    disruptor.handleEventsWith(handler)

    disruptor.start()
    val inTweetToDisruptor: ValueEventTweet = new ValueEventTweet(1,"I love Scala", "Chaminda W")

    val start = Instant.now
    println("started..")

    for (i <- 1 to 100) {
      disruptor.publishEvent(ValueEventTweetTranslator(inTweetToDisruptor))
    }

    val end = Instant.now
    val timeElapsed = Duration.between(start, end)
    println("Time taken is : " + timeElapsed.toMillis + " milliseconds")

    disruptor.shutdown()
    executor.shutdown()
}
