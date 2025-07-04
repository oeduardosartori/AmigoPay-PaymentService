package com.amigopay.payment.payment.validation;

import com.amigopay.payment.common.enums.ValidationMessage;
import com.amigopay.payment.exception.BusinessException;
import com.amigopay.payment.payment.dto.PaymentContext;
import com.amigopay.payment.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class DailyLimitValidator implements PaymentValidator {

    private final PaymentRepository paymentRepository;

    @Override
    public void validate(PaymentContext context) {
        var start = LocalDate.now().atStartOfDay();
        var end = LocalDate.now().atTime(LocalTime.MAX);

        BigDecimal totalToday = paymentRepository.sumTodayByPayer(context.payerId(), start, end);
        BigDecimal newTotal = totalToday.add(context.amount());

        if (newTotal.compareTo(BigDecimal.valueOf(20000)) > 0) {
            throw new BusinessException(ValidationMessage.DAILY_LIMIT_EXCEEDED);
        }
    }
}
