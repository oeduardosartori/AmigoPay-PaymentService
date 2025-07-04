package com.amigopay.payment.payment.service.impl;

import com.amigopay.payment.messaging.publisher.PaymentEventPublisher;
import com.amigopay.payment.payment.dto.PaymentRequest;
import com.amigopay.payment.payment.dto.PaymentResponse;
import com.amigopay.payment.payment.entity.Payment;
import com.amigopay.payment.payment.factory.PaymentFactory;
import com.amigopay.payment.payment.mapper.PaymentMapper;
import com.amigopay.payment.payment.repository.PaymentRepository;
import com.amigopay.payment.payment.service.PaymentService;
import com.amigopay.payment.payment.validation.CompositePaymentValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final CompositePaymentValidator validator;
    private final PaymentEventPublisher eventPublisher;

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {
        var context = PaymentMapper.toContext(request);
        validator.validate(context);

        Payment payment = PaymentFactory.createFrom(context);
        Payment saved = paymentRepository.save(payment);

        eventPublisher.publishInitiated(saved);

        return PaymentMapper.toResponse(saved);
    }
}
