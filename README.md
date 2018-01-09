# Stab-at-twitter-using-disruptor-actor-model

This is an example app that takes a stab at implementing an app like twitter:

The problem this tries to solve is:

Say Obama has 100 million followers. When he tweet how do you notify all his floowers of the event?
This might involves updating 100 million records on the back of Obama's tweet.

Design:

Diagram in : https://www.linkedin.com/pulse/stab-twitter-low-latency-execution-engine-chaminda-wijayasundara/?published=t

Message production:

- Single threaded producer push messages to disruptor / ring buffer. 
    - Single producer to the Disruptor (ProducerType.SINGLE)
    - BusySpinWaitStrategy (non blocking producer stretegy)
    - ring size (1024 * 128)
    - Increasing the ring size will improve the performance but under heavy load, messages can be lost (as Disruptor is in memory)
    - So set the ring size = number of event you want to process
    - If the number of events are large, then producer will wait till it gets a slot in case the ring buffer is full.
- Kafka message producer is an Actor.
    - Actors (run on many threads in parellel) picks up messages off the Disruptor.
    - Push these messages to the Kafka topic.
    
Message consumption

- Consumers off Kafka are Actors that run on multiple threads in parellel.
- These actors consume messages and save in Cassandra.
- Cassandra actor uses sync executor for the moment (session.execute(cqlInsert)) vs session.executeAsync() that returns a Future

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

10,000

    Time taken is to publish to Kafka is : 74 milliseconds
    Time taken to consume and update Cassandra: 34 milliseconds (actors work for few more secs in the background)

100,000

    Time taken is to publish to Kafka is : 22077 milliseconds - 22 secs
    Time taken to consume and update Cassandra: 230 milliseconds (actors work for few more secs in the background)

1,000,000

    Time taken is to publish to Kafka is : 215125 milliseconds (215.125 secs)
    Time taken to consume and update Cassandra: 2000 milliseconds (actors work for few more secs in the background)
    
After optimising the producer (convert the producers to actors + change the Disruptor publisher stretegy)

10,000

    Time taken is to publish to Kafka is : 8 milliseconds (actors work for few more secs in the background)
    Time taken to consume and update Cassandra: 30 milliseconds (actors work for few more secs in the background)

100,000

    Time taken is to publish to Kafka is : 23 milliseconds (actors work for few more secs in the background)
    Time taken to consume and update Cassandra: 230 milliseconds (actors work for few more secs in the background)

1,000,000

    Time taken is to publish to Kafka is : 365 milliseconds  (actors work for few more secs/mins in the background)
    Time taken to consume and update Cassandra: 2000 milliseconds (actors work for few more secs in the background)