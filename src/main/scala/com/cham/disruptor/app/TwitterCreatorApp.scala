package com.cham.disruptor.app

/**
  * Created by cwijayasundara on 27/12/2017.
  */

import com.cham.disruptor.app.MessageConsumerAllApp.kafkaMessageConsumerActor

object TwitterCreatorApp extends App {
  kafkaMessageConsumerActor ! "consume"
}
