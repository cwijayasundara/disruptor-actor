package com.cham.disruptor.app

import com.cham.disruptor.app.MessageConsumerAllApp.kafkaMessageConsumerActor

object TwitterCreatorApp extends App {

  kafkaMessageConsumerActor ! "consume"

}
