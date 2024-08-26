package com.example.config;


import com.example.domain.Transaction;
import com.example.domain.TransactionKey;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.ContainerCustomizer;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.util.backoff.FixedBackOff;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final KafkaProperties properties;
    // private final KafkaTemplate<TransactionKey, Transaction> template;

    // private DeadLetterPublishingRecoverer deadLetterPublishingRecoverer() {
    //     DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(template,
    //             (r, e) -> {
    //                 if (e.getCause() instanceof IllegalStateException) {
    //                     return new TopicPartition(r.topic() + ".RETRY", r.partition());
    //                 } else {
    //                     return new TopicPartition(r.topic() + ".DLT", r.partition());
    //                 }
    //             });
    //     return recoverer;
    // }

    // private CommonErrorHandler commonErrorHandler() {
    //     //var fixedBackOff = new FixedBackOff(1000, 2);
    //     var exponentialBackOff = new ExponentialBackOffWithMaxRetries(2);
    //     exponentialBackOff.setInitialInterval(1000);
    //     exponentialBackOff.setMultiplier(2.0);
    //     exponentialBackOff.setMaxInterval(2_000);
    //     var errorHandler = new DefaultErrorHandler(
    //             deadLetterPublishingRecoverer(),
    //             //fixedBackOff
    //             exponentialBackOff
    //     );
    //     errorHandler.setRetryListeners((record, exception, deliveryAttempt) -> {
    //         System.out.println("Delivery attempt: " + deliveryAttempt);
    //     });
//        var exceptionsNotToRetry = List.of(
//                IllegalArgumentException.class
//        );
//        var exceptionsToRetry = List.of(
//                IllegalStateException.class
//        );
//        exceptionsNotToRetry.forEach(errorHandler::addNotRetryableExceptions);
//        exceptionsToRetry.forEach(errorHandler::addRetryableExceptions);
        // return errorHandler;
    // }

    @Bean
    ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
            ObjectProvider<ConsumerFactory<Object, Object>> kafkaConsumerFactory,
            ObjectProvider<ContainerCustomizer<Object, Object, ConcurrentMessageListenerContainer<Object, Object>>> kafkaContainerCustomizer,
            ObjectProvider<SslBundles> sslBundles) {

        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, kafkaConsumerFactory.getIfAvailable(() -> new DefaultKafkaConsumerFactory<>(
                this.properties.buildConsumerProperties(sslBundles.getIfAvailable()))));
        kafkaContainerCustomizer.ifAvailable(factory::setContainerCustomizer);

        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);

       factory.setConcurrency(1);
        // factory.setCommonErrorHandler(commonErrorHandler());

        return factory;
    }


}
