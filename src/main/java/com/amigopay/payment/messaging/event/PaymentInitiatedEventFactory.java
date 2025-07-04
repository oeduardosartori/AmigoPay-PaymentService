package com.amigopay.payment.messaging.event;

import com.amigopay.events.PaymentInitiatedEvent;
import com.amigopay.payment.payment.entity.Payment;

public class PaymentInitiatedEventFactory {

    private PaymentInitiatedEventFactory() {}

    public static PaymentInitiatedEvent from(Payment payment) {
        return new PaymentInitiatedEvent(
                payment.getId(),
                payment.getPayerId(),
                payment.getPayeeId(),
                payment.getAmount(),
                payment.getCreatedAt()
        );
    }
}
