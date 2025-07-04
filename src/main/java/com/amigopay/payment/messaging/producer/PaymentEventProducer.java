package com.amigopay.payment.messaging.producer;

import com.amigopay.events.PaymentInitiatedEvent;

public interface PaymentEventProducer {
    void sendPaymentInitiatedEvent(PaymentInitiatedEvent event);
}
