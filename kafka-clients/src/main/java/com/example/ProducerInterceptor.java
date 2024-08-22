package com.example;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class ProducerInterceptor implements org.apache.kafka.clients.producer.ProducerInterceptor<String, String> {

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("ProducerInterceptor.onSend() called: " + record);
        //....
        // add headers to the record
        record.headers().add("location", "mombai".getBytes());
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println("ProducerInterceptor.onAcknowledgement() called: " + metadata + ", " + exception);
        //...
    }

    @Override
    public void close() {
        System.out.println("ProducerInterceptor.close() called");
    }

    @Override
    public void configure(Map<String, ?> configs) {
        System.out.println("ProducerInterceptor.configure() called: " + configs);
    }
    
}
