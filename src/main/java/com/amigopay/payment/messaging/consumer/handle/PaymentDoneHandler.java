package com.amigopay.payment.messaging.consumer.handle;

import com.amigopay.events.PaymentDoneEvent;
import com.amigopay.payment.common.enums.ValidationMessage;
import com.amigopay.payment.exception.BusinessException;
import com.amigopay.payment.payment.enums.PaymentStatus;
import com.amigopay.payment.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentDoneHandler {

    private final PaymentRepository paymentRepository;

    public void processPaymentDone(PaymentDoneEvent event) {
        var payment = paymentRepository.findById(event.paymentId())
                .orElseThrow(() -> new BusinessException(ValidationMessage.PAYMENT_NOT_FOUND));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            log.warn("Payment {} already processed with status {}", payment.getId(), payment.getStatus());
            return;
        }

        payment.complete();
        paymentRepository.save(payment);

        log.info("Payment {} marked as COMPLETED", payment.getId());
    }
}