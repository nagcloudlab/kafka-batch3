package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import redis.clients.jedis.Jedis;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaToRedisConsumer {

    private static KafkaConsumer<String, String> createKafkaConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "activity_group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("user-activity"));
        return consumer;
    }

    private static Jedis createRedisClient() {
        return new Jedis("localhost", 6379);
    }

    public static void main(String[] args) {
        KafkaConsumer<String, String> consumer = createKafkaConsumer();
        Jedis redisClient = createRedisClient();
        ObjectMapper objectMapper = new ObjectMapper();

        System.out.println("Kafka Consumer and Redis initialized. Waiting for messages...");

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                try {
                    JsonNode userActivity = objectMapper.readTree(record.value());
                    String userId = userActivity.get("user_id").asText();
                    String activityType = userActivity.get("activity_type").asText();
                    String timestamp = userActivity.get("timestamp").asText();
                    String redisKey = "user:" + userId + ":activity:" + activityType;
                    // Store in Redis (increment activity counter)
                    long count=redisClient.incr(redisKey);
                    // Set expiry for the key (e.g., 1 hour)
                    redisClient.expire(redisKey, 3600);
                    if(count>300){
                        redisClient.publish("user-activity-alert", "User " + userId + " has performed " + activityType + " 300 times!");
                    }
                    System.out.println("Processed activity for user " + userId + ": " + activityType + " at " + timestamp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
