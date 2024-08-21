package com.example;

import org.apache.kafka.common.Cluster;

public class CustomPartitioner implements org.apache.kafka.clients.producer.Partitioner {
   
    public void close() {
    }

    public void configure(java.util.Map<java.lang.String, ?> configs) {
    }

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // Custom logic to determine partition
        return 2;
    }
    
}
