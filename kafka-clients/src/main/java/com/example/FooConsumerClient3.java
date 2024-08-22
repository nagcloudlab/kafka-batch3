package com.example;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

public class FooConsumerClient3 {
    public static void main(String[] args) throws Exception {
    
        Properties properties = new Properties();

        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "foo");
        properties.put(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, "foo-3");
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true"); // true | false
        properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, CooperativeStickyAssignor.class.getName());
        
        properties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "15");
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "45");

        // control poll behavior
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "500");
        properties.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, "1");
        properties.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, "10485760");
        properties.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, "500");
        properties.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "500");


        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        
        // subscribe to topic(s)
        consumer.subscribe(java.util.Arrays.asList("topic1"));

         Runtime.getRuntime().addShutdownHook(new Thread(() -> {
           consumer.wakeup();
        }));

       try{
         // poll for new data
         while (true) {
             //System.out.println("Polling");
             ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
             //System.out.println("Polled " + records.count() + " records");
             // print each record : topic /t partition /t offset 

             // track processed offsets
             Map<TopicPartition, OffsetAndMetadata> currentProcessedOffsets = new HashMap<>(); // or redis...

             // Process Records either Sync or Async
             records.forEach(record -> {
                 System.out.println(record.topic() + "\t" + record.partition() + "\t" + record.offset());
                 // try {
                 //     TimeUnit.SECONDS.sleep(1);
                 // } catch (InterruptedException e) {
                 //     e.printStackTrace();
                 // }
                 currentProcessedOffsets.put(new TopicPartition(record.topic(), record.partition()),
                         new OffsetAndMetadata(record.offset() + 1));
             });

             // commit processed offsets
             //consumer.commitSync(currentProcessedOffsets); // sync | async

         }
    
    } catch (WakeupException e) {
        System.out.println("Shutting down");
    } finally {
        consumer.close();
    }

       

    }
}
