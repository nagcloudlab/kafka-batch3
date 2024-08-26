package com.example;

import java.util.Properties;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

public class ConsumerAndProducer {


    public static void main(String[] args) {

        // Consumer and Producer APIs

        // Properties consumerProperties = new Properties();
        // consumerProperties.put("bootstrap.servers", "localhost:9092");
        // consumerProperties.put("auto.offset.reset", "earliest");
        // consumerProperties.put("group.id", "numbers-group");
        // consumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // consumerProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProperties);

        // Properties producerProperties = new Properties();
        // producerProperties.put("bootstrap.servers", "localhost:9092");
        // producerProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // producerProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // KafkaProducer<String, String> producer = new KafkaProducer<>(producerProperties);

        // consumer.subscribe(List.of("numbers"));

        // Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        //     consumer.close();
        //     producer.close();
        // }));

        // while (true) {
        //     ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        //     for(var record : records) {
        //         int number = Integer.parseInt(record.value());
        //         if(number % 2 == 0) {
        //             producer.send(new org.apache.kafka.clients.producer.ProducerRecord<>("even-numbers", record.key(), record.value()));
        //         } else {
        //             producer.send(new org.apache.kafka.clients.producer.ProducerRecord<>("odd-numbers", record.key(), record.value()));
        //         }
        //     }
        // }


        // Streams API

        Properties streamsProperties = new Properties();
        streamsProperties.put("bootstrap.servers", "localhost:9092");
        streamsProperties.put("application.id", "numbers-streams");
        streamsProperties.put("default.key.serde", "org.apache.kafka.common.serialization.Serdes$StringSerde");
        streamsProperties.put("default.value.serde", "org.apache.kafka.common.serialization.Serdes$StringSerde");
        // streams threads
        streamsProperties.put("num.stream.threads", "3"); // scalability
        // exactly-once processing
        streamsProperties.put("processing.guarantee", "exactly_once_v2"); // exactly-once processing


        // processor topology
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> numbers = builder.stream("numbers"); // source processor

        numbers
                .filter((key, value) -> Integer.parseInt(value) % 2 == 0) // processor
                .to("even-numbers"); // sink processor

        numbers
                .filter((key, value) -> Integer.parseInt(value) % 2 != 0)
                .to("odd-numbers");

        Topology topology = builder.build();

        KafkaStreams streams = new KafkaStreams(topology, streamsProperties); // consumer and producer
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            streams.close();
        }));

    }
}

// data/stream processing

// filtering
// transformation
// aggregation
// joining
// windowing


// stateful processing
// exactly-once processing
// real-time
// fault-tolerant
// scalable
// distributed


// with consumer & producer apis, we should be very careful while processing stream


// with streams-api, we can do all these things easily
// - this api implementation available in in java
// ksqldb - sql like language for kafka streams
