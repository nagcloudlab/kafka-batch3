package com.example;

import java.util.Properties;
import java.util.List;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

public class ProducerClient {
    public static void main(String[] args) {
        
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.example.CustomPartitioner");

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
        
        for (int i = 0; i < 1000000; i++) {
            String value = new String(new char[1024]).replace('\0', 'a');
            String key = List.of("key1", "key2", "key3").get(i % 3);
            ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic,key,value);
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    exception.printStackTrace();
                } else {
                    System.out.println("Record sent to partition " + metadata.partition() + " with offset " + metadata.offset());
                }
            });
        }

        producer.close();


    }
}
