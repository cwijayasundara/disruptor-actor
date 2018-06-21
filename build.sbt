name := """stab-at-twitter-disruptor-actor-model"""

version := "1.0.0"

scalaVersion := "2.11.8"

resolvers += "OSS Sonatype" at "https://repo1.maven.org/maven2/"

libraryDependencies ++= {
  Seq(
    "com.google.code.gson" %  "gson"                    % "2.8.0"  withSources(),
    "org.apache.kafka" % "kafka_2.11" % "0.10.1.0" withSources(),
    "com.typesafe.akka" % "akka-actor_2.11" % "2.4.14" withSources(),
    "com.typesafe.akka" % "akka-cluster_2.11" % "2.4.14" withSources(),
    "com.lmax" % "disruptor" % "3.3.6" withSources(),
    "com.datastax.cassandra"  % "cassandra-driver-core" % "3.0.0"  exclude("org.xerial.snappy", "snappy-java"),
    "io.spray" %%  "spray-json" % "1.3.3",
     "com.hazelcast.jet" % "hazelcast-jet" % "0.6"
  )
}


