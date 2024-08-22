package com.example;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

public class ConsumerClient {
    public static void main(String[] args) throws Exception {
    
        Properties properties = new Properties();

        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group2");
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

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
            records.forEach(record -> {
                System.out.println(record.topic() + "\t" + record.partition() + "\t" + record.offset());
                // try {
                //     TimeUnit.SECONDS.sleep(1);
                // } catch (InterruptedException e) {
                //     e.printStackTrace();
                // }
            });
            
        }
    } catch (WakeupException e) {
        System.out.println("Shutting down");
    } finally {
        consumer.close();
    }

       

    }
}
