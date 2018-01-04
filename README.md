# Stab-at-twitter-using-disruptor-actor-model

This is an example app that takes a stab at implementing an app like twitter:

The problem this tries to solve is:

Say Obama has 100 million followers. When he tweet how do you notify all his floowers of the event?
This might involves updating 100 million records on the back of Obama's tweet.

Design: << to do>>

Set Up:

Starting Zookeper:
go to zookeeper/3.4.10/bin
    Server start

start kafka: go to kafka/0.11.0.1/bin

    kafka-server-start /usr/local/etc/kafka/server.properties --override property= 

Basic Kafka commands:

    kafka-topics --list --zookeeper localhost:2181

    kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic tweet-publisher-topic

    kafka-topics --zookeeper localhost:2181 --describe --topic tweet-publisher-topic

    kafka-topics --zookeeper 127.0.0.1:2181 --delete --topic tweet-topic

    kafka-console-consumer --zookeeper localhost:2181 --topic tweet-publisher-topic  --from-beginning

    kafka-topics --zookeeper localhost:2181 --alter --topic tweet-topic  --config retention.ms=1000

    kafka-topics --zookeeper localhost:2181  --alter --topic tweet-topic --delete-config retention.ms

Start Cassandra:

    type cassandra and press enter
    cqlsh to get to the cql
    
list all the Cassandra key spaces:

    cqlsh> DESCRIBE keyspaces;

Stats running the code in my machine (MacOsX- High Sierra 10.13.2; 2.2 GHz Intel Core i7)

10,000 messages
    - push to kafka: 74 milli seconds
    - save to cassandra : 34 milliseconds

100,000 messages
    - push to kafka: : 22077 milliseconds (22 seconds)
    - save to cassandra: 250 milliseconds

1,000,000 messages
    - push to kafka: : 215125 milliseconds (215.125 secs)
    - save to cassandra: 2000 milliseconds
