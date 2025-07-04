package com.amigopay.payment.messaging.config;

import com.amigopay.events.PaymentDoneEvent;
import com.amigopay.events.PaymentRejectedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    // PaymentDoneEvent Consumer
    @Bean
    public ConsumerFactory<String, PaymentDoneEvent> paymentDoneEventConsumerFactory() {
        return new org.springframework.kafka.core.DefaultKafkaConsumerFactory<>(consumerConfigs(PaymentDoneEvent.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentDoneEvent> paymentDoneKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, PaymentDoneEvent>();
        factory.setConsumerFactory(paymentDoneEventConsumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setAckMode(org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    // PaymentRejectedEvent Consumer
    @Bean
    public ConsumerFactory<String, PaymentRejectedEvent> paymentRejectedEventConsumerFactory() {
        return new org.springframework.kafka.core.DefaultKafkaConsumerFactory<>(consumerConfigs(PaymentRejectedEvent.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentRejectedEvent> paymentRejectedKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, PaymentRejectedEvent>();
        factory.setConsumerFactory(paymentRejectedEventConsumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setAckMode(org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    private <T> Map<String, Object> consumerConfigs(Class<T> clazz) {
        var configs = new HashMap<String, Object>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "payment-consumer");
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configs.put(JsonDeserializer.TRUSTED_PACKAGES, "com.amigopay.events");
        configs.put(JsonDeserializer.VALUE_DEFAULT_TYPE, clazz.getName());
        configs.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return configs;
    }
}

