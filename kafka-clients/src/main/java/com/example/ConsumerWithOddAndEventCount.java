package com.example;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;

public class ConsumerWithOddAndEventCount {

    public static void main(String[] args) {
        
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test-group");
        // deserializer
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // long deserializer
        props.put("value.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");

        KafkaConsumer<String, Long> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(List.of("odd-even-count"));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            consumer.close();
        }));

        while (true) {
            var records = consumer.poll(Duration.ofMillis(100));
            for (var record : records) {
                System.out.println("Key: " + record.key() + ", Value: " + record.value());
            }
        }



        

    }
    
}
