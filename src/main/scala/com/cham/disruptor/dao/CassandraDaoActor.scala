package com.cham.disruptor.dao

/**
  * Created by cwijayasundara on 27/12/2017.
  */

import akka.actor.{Actor, ActorLogging, Props}
import com.datastax.driver.core.Cluster
import akka.util.Timeout
import java.util.UUID.randomUUID

import com.cham.disruptor.dto.TweetMessage

object CassandraDaoActorBuilder {
  def props(implicit timeout: Timeout) =  Props(new CassandraDaoActor())
  def name = "cassandraDaoActor"
}

class CassandraDaoActor extends Actor with ActorLogging{

  implicit val session = new Cluster
                        .Builder()
                        .addContactPoints("127.0.0.1")
                        .withPort(9042)
                        .build()
                        .connect()

  def saveToCassandra(message: TweetMessage): Unit= {

    val guid = randomUUID.toString
    val tweetId = message.messageId
    val tweet = message.tweet
    val user = message.user

    val cqlInsert = "INSERT INTO disruptor.tweets(id,tweetid,user,tweet) values("+"'" + guid +"'" + "," +"'"+ tweetId +"'"+ "," +"'"+ user +"'"+ "," +"'"+ tweet +"'"+ ")"
    val insertResult = session.execute(cqlInsert)

  }

  def selectFromCassandra(select: String) = {
    println("inside selectFromCassandra ..")
    val cqlInsert = "select * from disruptor.tweets"
    val result = session.execute(cqlInsert)
    println("size of the result set from Cassandra  is " + result.all().size())
  }

  override def receive : Actor.Receive = {
    case message: TweetMessage => saveToCassandra(message)
    case "get"  => selectFromCassandra("get")
    case "delete" => deleteFromCassandra("delete")
  }

  def deleteFromCassandra(delete: String) = {
    val cqlDelete = "TRUNCATE disruptor.tweets"
    val result = session.execute(cqlDelete)
  }

}
