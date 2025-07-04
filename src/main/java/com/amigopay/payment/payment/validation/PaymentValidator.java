package com.amigopay.payment.payment.validation;

import com.amigopay.payment.payment.dto.PaymentContext;

public interface PaymentValidator {
    void validate(PaymentContext context);
}
