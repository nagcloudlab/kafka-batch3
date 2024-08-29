package com.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

import java.util.Properties;
import java.util.HashMap;
import java.util.Map;

public class FooConsumer {
    public static void main(String[] args) throws Exception {

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "4.247.148.49:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "foo");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,CooperativeStickyAssignor.class.getName());
        

        properties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "3000");
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "45000");

        // static group membership
        //properties.put(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, "foo-1");

        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        //properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000");

        // How to control polling behavior
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "500");
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "300000");
        properties.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, "1");
        properties.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, "52428800");
        properties.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, "500");
        properties.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "1048576");


        // rack awareness
        //properties.put(ConsumerConfig.CLIENT_RACK_CONFIG, "rack1");


        // other configurations
        //properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "foo-consumer");
        // retry, retry.backoff.ms, request.timeout.ms

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>(); // redis

        consumer.subscribe(java.util.Collections.singletonList("topic1"),new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(java.util.Collection<TopicPartition> partitions) {
                System.out.println("onPartitionsRevoked");
                //consumer.commitSync(offsets);
            }

            @Override
            public void onPartitionsAssigned(java.util.Collection<TopicPartition> partitions) {
                System.out.println("onPartitionsAssigned");
                // initialization
            }

        });

        // add a shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            consumer.wakeup();
        }));

        try{
            while (true) {
                //System.out.println("polling");
                // cache
                ConsumerRecords<String, String> records = consumer.poll(java.time.Duration.ofMillis(1000));
                //System.out.println("records count: " + records.count());
                records.forEach(record -> {
                    // print topic, partition, offset
                    System.out.println(record.topic() + "\t" + record.partition() + "\t" + record.offset());
                    offsets.put(new TopicPartition(record.topic(), record.partition()),
                            new OffsetAndMetadata(record.offset() + 1));
                });
                //consumer.commitSync(offsets); // commit offset request
                //TimeUnit.SECONDS.sleep(1);
            }
        } catch (WakeupException e){
            //...
        } finally {
            System.out.println("closing consumer");
            consumer.close(); // leave the group
        }
    }
}
