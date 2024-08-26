package com.example.config;

import com.example.domain.Transaction;
import com.example.domain.TransactionKey;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Properties;

@Configuration
public class KafkaConfig {

//    @Bean
//    public KafkaProducer<TransactionKey, Transaction> kafkaProducer() {
//        Properties properties = new Properties();
//        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        return new KafkaProducer<>(properties);
//    }


    // @Bean
    // public NewTopic transactionEvents(){
    //     return TopicBuilder.name("transactions")
    //             .partitions(3)
    //             //.replicas(3)
    //             //.config("min.insync.replicas", "2")
    //             .build();
    // }

}
