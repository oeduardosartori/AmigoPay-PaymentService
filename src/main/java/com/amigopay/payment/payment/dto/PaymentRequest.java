package com.amigopay.payment.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(
        @NotNull(message = "validation.payer.required")
        UUID payerId,

        @NotNull(message = "validation.payee.required")
        UUID payeeId,

        @NotNull(message = "validation.amount.required")
        @DecimalMin(value = "0.01", message = "validation.payment.amount.greater.than.zero")
        BigDecimal amount
)
{ }
