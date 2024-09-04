package com.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Demo1 {

    public static void main(String[] args) {

        // Producer properties
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        producerProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        producerProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "transactional-transfer-processor");

        // Consumer properties
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "transfer-processor-group");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        consumerProps.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps);

        consumer.subscribe(Collections.singletonList("transfers"));

        producer.initTransactions();

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    producer.beginTransaction();

                    try {
                        // Parse the transfer message, e.g., "account1->account2:100"
                        String[] parts = record.value().split("->|:");
                        String debitAccount = parts[0];
                        String creditAccount = parts[1];
                        String amount = parts[2];

                        // Create debit and credit messages
                        String debitMessage = "debit:" + debitAccount + ":" + amount;
                        String creditMessage = "credit:" + creditAccount + ":" + amount;

                        // Send debit and credit messages to the 'balances' topic
                        producer.send(new ProducerRecord<>("balances", "debit", debitMessage));// balnces[0] = debit
                        producer.send(new ProducerRecord<>("balances", "credit", creditMessage)); // balnces[1] = credit


                        // Commit the offsets for the consumed record within the transaction
                        Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
                        offsets.put(new TopicPartition(record.topic(), record.partition()),
                                new OffsetAndMetadata(record.offset() + 1));

                        producer.sendOffsetsToTransaction(offsets, "transfer-processor-group");

                        if(false){
                            throw new RuntimeException("Simulating an error");
                        }
                        //consumer.commitSync(offsets);
                        // Commit the transaction
                        producer.commitTransaction();
                        System.out.println("Transaction committed successfully for: " + record.value());
                    } catch (Exception e) {
                        producer.abortTransaction();
                        System.out.println("Transaction aborted for: " + record.value());
                    }
                }
            }
        } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
            producer.close();
        } finally {
            producer.close();
            consumer.close();
        }
    }
}
