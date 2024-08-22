package com.example;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.concurrent.TimeUnit;

public class ProducerClient {
    public static void main(String[] args) throws Exception {

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        //properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class.getName());
        
        //-------------------------------------------
        // safe ( durability ) producer
        //-------------------------------------------

        // - create topic with replication factor > 1
        // - acks=all
        // - producer config: retries, retry.backoff.ms, delivery.timeout.ms
        // - min.insync.replicas=2 ( topic / broker level)
        // - unclean.leader.election.enable=false ( broker level)
        // - enable.idempotency=true
        
        properties.put(ProducerConfig.ACKS_CONFIG, "-1"); // 0, 1,-1/all
        properties.put(ProducerConfig.RETRIES_CONFIG, "2147483647");
        properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, "1000");
        properties.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, "120000");
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        
        //-------------------------------------------
        // high throughput producer
        //-------------------------------------------

        // - batch.size=16384 
        // - linger.ms=0
        // - compression.type=none
        // - max.in.flight.requests.per.connection=5
        // - buffer.memory=33554432
        // - max.block.ms=60000


        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        properties.put(ProducerConfig.LINGER_MS_CONFIG, "0");
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "none"); // producer | topic | broker
        properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1");
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, "60000");


        //-------------------------------------------
        // other configuration
        //-------------------------------------------

        // 
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "producer-1");
        properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, "com.example.ProducerInterceptor");



        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        
        // way-1: Synchronous
        // String topic = "topic1";
        // String value = "Hello, Kafka!";
        // ProducerRecord<String, String> record = new ProducerRecord<>(topic, value);
        // Future<RecordMetadata> future = producer.send(record); // Asynchronous
        // try {
        //     RecordMetadata metadata = future.get(); // Blocking
        //     System.out.println("Record sent to partition " + metadata.partition() + " with offset " + metadata.offset());
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }


        // way-2: Asynchronous    
        String topic = "topic1";
        for (int i = 0; i < 1000000; i++) {
            // 1k sized message
            String value = "Apache Kafka is a distributed event store and stream-processing platform. It is an open-source system developed by the Apache Software Foundation written in Java and Scala. The project aims to provide a unified, high-throughput, low-latency platform for handling real-time data feed\n"
                    +
                    "Apache Kafka is a distributed event store and stream-processing platform. It is an open-source system developed by the Apache Software Foundation written in Java and Scala. The project aims to provide a unified, high-throughput, low-latency platform for handling real-time data feed\n"
                    +
                    "Apache Kafka is a distributed event store and stream-processing platform. It is an open-source system developed by the Apache Software Foundation written in Java and Scala. The project aims to provide a unified, high-throughput, low-latency platform for handling real-time data feed\n"
                    +
                    "Apache Kafka is a distributed event store and stream-processing platform. It is an open-source system developed by the Apache Software Foundation write";
            //String key=List.of("key1","key2","key3").get(i%3);    
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, value);
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    exception.printStackTrace();
                } else {
                    System.out.println(
                            "Record sent to partition " + metadata.partition() + " with offset " + metadata.offset());
                }
            });
            TimeUnit.MILLISECONDS.sleep(1);
        }
        
        producer.close();

    }
}
