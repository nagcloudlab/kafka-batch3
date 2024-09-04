package com.example;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.AbstractKafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class CustomUserDeserializer implements Deserializer<User> {

    private KafkaAvroDeserializer avroDeserializer;
    private SchemaRegistryClient schemaRegistryClient;

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        schemaRegistryClient = new CachedSchemaRegistryClient(
                (String) configs.get("schema.registry.url"), 100);
        avroDeserializer = new KafkaAvroDeserializer(schemaRegistryClient, configs);
    }

    @Override
    public User deserialize(String topic, byte[] data) {
        if (data == null || data.length < 5) {
            return null;
        }

        // Extract the schema ID from the first 4 bytes after the magic byte
        int schemaId = ((data[1] & 0xFF) << 24) |
                ((data[2] & 0xFF) << 16) |
                ((data[3] & 0xFF) << 8)  |
                (data[4] & 0xFF);

        // You can now use the schemaId as needed
        System.out.println("Schema ID: " + schemaId);

        // Deserialize the data into a User object
        return (User) avroDeserializer.deserialize(topic, data);
    }

    @Override
    public void close() {
        avroDeserializer.close();
    }
}
