package com.cham.disruptor.app

import com.cham.disruptor.app.MessageConsumerAllApp.kafkaMessageConsumerActor


object TwitterRetreaverApp extends App {

  kafkaMessageConsumerActor ! "get"

}
