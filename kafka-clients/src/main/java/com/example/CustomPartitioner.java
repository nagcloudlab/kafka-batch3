package com.example;

import org.apache.kafka.common.Cluster;

public class CustomPartitioner implements org.apache.kafka.clients.producer.Partitioner {
   
    public void close() {
    }

    public void configure(java.util.Map<java.lang.String, ?> configs) {
    }

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // custom partitioning logic
        int partition = 0;
        // if key is not null
        if (key != null) {
            partition = Math.abs(key.hashCode()) % cluster.partitionCountForTopic(topic);
        }
        return partition;
    }
    
}
