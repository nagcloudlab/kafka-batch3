package com.example;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import java.util.List;
import java.util.Properties;

public class Demo2 {

    public static void main(String[] args) {
        // Define the properties for Kafka Streams
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "transactional-stream-processor");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE_V2);

        // Create a StreamsBuilder
        StreamsBuilder builder = new StreamsBuilder();

        // Define the input stream from the 'transfers' topic
        KStream<String, String> transfersStream = builder.stream("transfers");

        // Process the stream to split into debit and credit messages
        KStream<String, String>[] branchedStreams = transfersStream.flatMap((key, value) -> {

            String[] parts = value.split("->|:");
            String debitAccount = parts[0];
            String creditAccount = parts[1];
            String amount = parts[2];

            // Create debit and credit messages
            String debitMessage = "debit:" + debitAccount + ":" + amount;
            String creditMessage = "credit:" + creditAccount + ":" + amount;

            // Produce two records: one for debit and one for credit
            return List.of(
                    new KeyValue<>("debit", debitMessage),
                    new KeyValue<>("credit", creditMessage)
            );
        }).branch(
                (key, value) -> value.startsWith("debit"),
                (key, value) -> value.startsWith("credit")
        );

        // Send the debit messages to 'balances' topic partition 0
        branchedStreams[0].to("balances", Produced.with(Serdes.String(), Serdes.String()));

        // Send the credit messages to 'balances' topic partition 1
        branchedStreams[1].to("balances", Produced.with(Serdes.String(), Serdes.String()));

        // Build the topology and start the stream
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        // Add shutdown hook to gracefully stop the streams
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}

