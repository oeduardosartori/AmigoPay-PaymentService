package com.amigopay.payment.payment.validation;

import com.amigopay.payment.common.enums.ValidationMessage;
import com.amigopay.payment.exception.BusinessException;
import com.amigopay.payment.payment.dto.PaymentContext;
import org.springframework.stereotype.Component;

@Component
public class SamePersonValidator implements PaymentValidator {

    @Override
    public void validate(PaymentContext context) {
        if (context.payerId().equals(context.payeeId())) {
            throw new BusinessException(ValidationMessage.PAYER_AND_PAYEE_MUST_BE_DIFFERENT);
        }
    }
}
