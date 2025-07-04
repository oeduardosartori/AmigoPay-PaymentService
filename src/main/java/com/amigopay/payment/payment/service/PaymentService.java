package com.amigopay.payment.payment.service;

import com.amigopay.payment.payment.dto.PaymentRequest;
import com.amigopay.payment.payment.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse createPayment(PaymentRequest request);
}
