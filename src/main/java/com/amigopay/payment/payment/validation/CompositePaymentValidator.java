package com.amigopay.payment.payment.validation;

import com.amigopay.payment.payment.dto.PaymentContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompositePaymentValidator {

    private final List<PaymentValidator> validators;

    public void validate(PaymentContext context) {
        for (PaymentValidator validator : validators) validator.validate(context);
    }
}
