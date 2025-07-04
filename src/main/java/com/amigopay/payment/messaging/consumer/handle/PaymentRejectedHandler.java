package com.amigopay.payment.messaging.consumer.handle;

import com.amigopay.events.PaymentRejectedEvent;
import com.amigopay.payment.common.enums.ValidationMessage;
import com.amigopay.payment.payment.repository.PaymentRepository;
import com.amigopay.payment.payment.enums.PaymentStatus;
import com.amigopay.payment.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentRejectedHandler {

    private final PaymentRepository paymentRepository;

    public void processPaymentRejected(PaymentRejectedEvent event) {
        var payment = paymentRepository.findById(event.paymentId())
                .orElseThrow(() -> new BusinessException(ValidationMessage.PAYMENT_NOT_FOUND));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            log.warn("Payment {} already processed with status {}", payment.getId(), payment.getStatus());
            return;
        }

        payment.rejected();
        paymentRepository.save(payment);

        log.info("Payment {} marked as REJECTED. Reason: {}", payment.getId(), event.rejectionReason());
    }
}
