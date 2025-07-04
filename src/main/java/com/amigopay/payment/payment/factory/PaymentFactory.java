package com.amigopay.payment.payment.factory;

import com.amigopay.payment.payment.dto.PaymentContext;
import com.amigopay.payment.payment.entity.Payment;
import com.amigopay.payment.payment.enums.PaymentStatus;

import java.math.RoundingMode;

public class PaymentFactory {

    private PaymentFactory() {}

    public static Payment createFrom(PaymentContext context) {
        return Payment.builder()
                .payerId(context.payerId())
                .payeeId(context.payeeId())
                .amount(context.amount().setScale(2, RoundingMode.HALF_EVEN))
                .status(PaymentStatus.PENDING)
                .build();
    }
}
