package com.amigopay.payment.messaging.producer.impl;

import com.amigopay.events.PaymentInitiatedEvent;
import com.amigopay.payment.common.enums.ValidationMessage;
import com.amigopay.payment.exception.BusinessException;
import com.amigopay.payment.messaging.producer.PaymentEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaPaymentEventProducer implements PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "payment.initiated";

    @Override
    public void sendPaymentInitiatedEvent(PaymentInitiatedEvent event) {
        try {
            kafkaTemplate.send(TOPIC, event.paymentId().toString(), event);
            log.info("[Kafka] Event sent to topic '{}': {}", TOPIC, event);
        } catch (BusinessException ex) {
            log.error("[Kafka] Failed to send event to '{}': {}", TOPIC, ex.getMessage(), ex);
            throw new BusinessException(ValidationMessage.FAILED_PUBLISH_SEND_EVENT, ex);
        }
    }
}
