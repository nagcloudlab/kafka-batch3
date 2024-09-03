package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;

class UserActivity {
    private String user_id;
    private String activity_type;
    private String timestamp;
    public UserActivity(String user_id, String activity_type, String timestamp) {
        this.user_id = user_id;
        this.activity_type = activity_type;
        this.timestamp = timestamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getActivity_type() {
        return activity_type;
    }

    public String getTimestamp() {
        return timestamp;
    }
}


public class RandomUserActivityProducer {

    private static final String TOPIC = "user-activity";
    private static final String BOOTSTRAP_SERVERS = "localhost:9094";

    private static KafkaProducer<String, String> createKafkaProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    private static String generateRandomUserActivity() {
        Random random = new Random();
        ObjectMapper objectMapper = new ObjectMapper();

        String[] activityTypes = {"login", "logout", "transfer"};
        String userId = String.valueOf(10 + random.nextInt(10));
        String activityType = activityTypes[random.nextInt(activityTypes.length)];
        String timestamp = String.valueOf(System.currentTimeMillis());

        UserActivity userActivity = new UserActivity(userId, activityType, timestamp);

        try {
            return objectMapper.writeValueAsString(userActivity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        KafkaProducer<String, String> producer = createKafkaProducer();

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            String activityJson = generateRandomUserActivity();

            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, activityJson);

            try {
                RecordMetadata metadata = producer.send(record).get();
                System.out.println("Sent record(key=" + record.key() + " value=" + record.value() +
                        ") meta(partition=" + metadata.partition() + ", offset=" + metadata.offset() + ")");
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            // Sleep for a random interval to simulate user activity
            try {
                Thread.sleep(100 /*+ new Random().nextInt(4000)*/);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        producer.close();
    }
}
