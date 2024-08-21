package com.example;

import java.util.Properties;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.utils.Time;


public class ProducerClient {
    public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "ProducerClient");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.example.CustomPartitioner");

        // safety ( durability )
        props.put(ProducerConfig.ACKS_CONFIG, "-1"); // 0, 1, all/-1
        props.put(ProducerConfig.RETRIES_CONFIG, 2147483647);
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 100);
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120000);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);

        // performance ( throughput )
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "none");
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 0);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, "60000");


        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

        // Send a message (synchronously)
        // String topic = "topic1";
        // String value = "Hello, Kafka!";
        // ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, value);
        // Future<RecordMetadata> future = producer.send(record);
        // try {
        //     RecordMetadata metadata = future.get();
        //     System.out.println("Record sent to partition " + metadata.partition() + " with offset " + metadata.offset());
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        // Send a message (asynchronously)
        String topic = "topic1";

        // Why we need key in the record?
        // - to ensure message processing order
        // - to perfor stateful processing

        for (int i = 0; i < 32; i++) {
            String value = "Apache Kafka is a distributed event store and stream-processing platform. It is an open-source system developed by the Apache Software Foundation written in Java and Scala. The project aims to provide a unified, high-throughput, low-latency platform for handling real-time data feed\n" +
                    "Apache Kafka is a distributed event store and stream-processing platform. It is an open-source system developed by the Apache Software Foundation written in Java and Scala. The project aims to provide a unified, high-throughput, low-latency platform for handling real-time data feed\n" +
                    "Apache Kafka is a distributed event store and stream-processing platform. It is an open-source system developed by the Apache Software Foundation written in Java and Scala. The project aims to provide a unified, high-throughput, low-latency platform for handling real-time data feed\n" +
                    "Apache Kafka is a distributed event store and stream-processing platform. It is an open-source system developed by the Apache Software Foundation write";
            ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, 0, null, value);
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    exception.printStackTrace();
                } else {
                    System.out.println("Record sent to partition " + metadata.partition() + " with offset " + metadata.offset());
                }
            });
            //TimeUnit.SECONDS.sleep(1);
        }

        producer.close();


    }
}
