package com.cham.disruptor.app

/**
  * Created by cwijayasundara on 27/12/2017.
  */

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import com.cham.disruptor.consumer.{KafkaConsumerActor}

object MessageConsumerAllApp {
  implicit val system = ActorSystem()
  val kafkaMessageConsumerActor = system.actorOf(KafkaConsumerActorBuilder.props(Timeout.zero), KafkaConsumerActorBuilder.name)
}

object KafkaConsumerActorBuilder {
  def props(implicit timeout: Timeout) =  Props(new KafkaConsumerActor())
  def name = "kafkaConsumerActor"
}
