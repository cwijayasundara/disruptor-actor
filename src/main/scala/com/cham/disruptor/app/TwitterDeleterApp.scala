package com.cham.disruptor.app

import com.cham.disruptor.app.MessageConsumerAllApp.kafkaMessageConsumerActor

object TwitterDeleterApp extends App {

  kafkaMessageConsumerActor ! "delete"

}
