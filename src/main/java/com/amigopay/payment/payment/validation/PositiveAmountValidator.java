package com.amigopay.payment.payment.validation;

import com.amigopay.payment.common.enums.ValidationMessage;
import com.amigopay.payment.exception.BusinessException;
import com.amigopay.payment.payment.dto.PaymentContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PositiveAmountValidator implements PaymentValidator {

    @Override
    public void validate(PaymentContext context) {
        if (context.amount() == null || context.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ValidationMessage.PAYMENT_AMOUNT_GREATER_THAN_ZERO);
        }
    }
}
