package com.cloudera.flink.scala.example

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

object FlinkKafkaSimpleStreamLocal {

  def main(args: Array[String]): Unit = {
    
    val brokers = args(0)
    val topic = args(1)
    
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    
    System.setProperty("java.security.auth.login.config", "./src/main/resources/jaas.conf")
    
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", brokers)
    properties.setProperty("group.id", "flink-test")
    properties.setProperty("security.protocol", "SASL_SSL")
    properties.setProperty("sasl.mechanism", "PLAIN")
    properties.setProperty("sasl.kerberos.service.name", "kafka")
    properties.setProperty("ssl.truststore.location",
       "./src/main/resources/truststore.jks")
    
    val myConsumer = new FlinkKafkaConsumer(topic, new SimpleStringSchema(), properties)
    
    myConsumer.setStartFromEarliest()
    
    val stream = env.addSource(myConsumer)
    
    stream.print()
    
    env.execute()
    
  }

}
