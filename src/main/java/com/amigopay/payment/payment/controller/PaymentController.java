package com.amigopay.payment.payment.controller;

import com.amigopay.payment.payment.dto.PaymentRequest;
import com.amigopay.payment.payment.dto.PaymentResponse;
import com.amigopay.payment.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping()
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        log.info("Received peyment creation request for payerYd={} to payeeId={} amount={}",
                request.payerId(), request.payeeId(), request.amount()
        );

        PaymentResponse response = paymentService.createPayment(request);
        return ResponseEntity.status(201).body(response);
    }
}
