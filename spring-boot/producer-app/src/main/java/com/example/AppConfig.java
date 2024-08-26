package com.example;

import com.example.domain.Transaction;
import com.example.domain.TransactionKey;
import com.example.service.TransactionService;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

//@Configuration
public class AppConfig {

//    @Bean
//    public TransactionService transactionProducer(@Value("${transaction-producer.topic}") String topic,
//                                                  KafkaProducer<TransactionKey, Transaction> kafkaProducer) {
//        return new TransactionService(topic, kafkaProducer);
//    }

}
