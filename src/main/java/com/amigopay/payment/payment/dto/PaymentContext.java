package com.amigopay.payment.payment.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentContext(
        UUID payerId,
        UUID payeeId,
        BigDecimal amount
) { }
