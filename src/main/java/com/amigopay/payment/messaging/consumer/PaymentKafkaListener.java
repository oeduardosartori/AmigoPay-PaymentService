package com.amigopay.payment.messaging.consumer;

import com.amigopay.events.PaymentDoneEvent;
import com.amigopay.events.PaymentRejectedEvent;
import com.amigopay.payment.messaging.consumer.handle.PaymentDoneHandler;
import com.amigopay.payment.messaging.consumer.handle.PaymentRejectedHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentKafkaListener {

    private final PaymentDoneHandler doneHandler;
    private final PaymentRejectedHandler rejectedHandler;

    @KafkaListener(
            topics = "payment.done",
            groupId = "payment-consumer",
            containerFactory = "paymentDoneKafkaListenerContainerFactory"
    )
    public void listnerPaymentDone(
            ConsumerRecord<String, PaymentDoneEvent> record,
            Acknowledgment ack
    ) {
        log.info("[Kafka] Received PaymentDoneEvent: {}", record.value());
        doneHandler.processPaymentDone(record.value());
        ack.acknowledge();
    }

    @KafkaListener(
            topics = "payment.rejected",
            groupId = "payment-consumer",
            containerFactory = "paymentRejectedKafkaListenerContainerFactory"
    )
    public void listenPaymentRejected(
            ConsumerRecord<String, PaymentRejectedEvent> record,
            Acknowledgment ack
    ) {
        log.info("[Kafka] Received PaymentRejectedEvent: {}", record.value());
        rejectedHandler.processPaymentRejected(record.value());
        ack.acknowledge();
    }
}
