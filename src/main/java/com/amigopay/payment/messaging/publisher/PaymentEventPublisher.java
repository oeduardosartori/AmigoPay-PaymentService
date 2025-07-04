package com.amigopay.payment.messaging.publisher;

import com.amigopay.events.PaymentInitiatedEvent;
import com.amigopay.payment.common.enums.ValidationMessage;
import com.amigopay.payment.exception.BusinessException;
import com.amigopay.payment.messaging.event.PaymentInitiatedEventFactory;
import com.amigopay.payment.messaging.producer.PaymentEventProducer;
import com.amigopay.payment.payment.entity.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {

    private final PaymentEventProducer eventProducer;

    public void publishInitiated(Payment payment) {
        try {
            PaymentInitiatedEvent event = PaymentInitiatedEventFactory.from(payment);
            eventProducer.sendPaymentInitiatedEvent(event);
        } catch (BusinessException ex) {
            log.error("[Kafka] Failed to publish PaymentInitiatedEvent for payment {}: {}", payment.getId(), ex.getMessage(), ex);
            throw new BusinessException(ValidationMessage.FAILED_PUBLISH_SEND_EVENT);
        }
    }
}
