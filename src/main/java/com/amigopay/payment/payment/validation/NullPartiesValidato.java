package com.amigopay.payment.payment.validation;

import com.amigopay.payment.common.enums.ValidationMessage;
import com.amigopay.payment.exception.BusinessException;
import com.amigopay.payment.payment.dto.PaymentContext;
import org.springframework.stereotype.Component;

@Component
public class NullPartiesValidato implements PaymentValidator{

    @Override
    public void validate(PaymentContext context) {
        if (context.payerId() == null || context.payeeId() == null) {
            throw new BusinessException(ValidationMessage.PAYER_AND_PAYEE_CANNOT_BE_NULL);
        }
    }
}
