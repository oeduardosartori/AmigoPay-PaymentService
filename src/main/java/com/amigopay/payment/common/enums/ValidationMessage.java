package com.amigopay.payment.common.enums;

public enum ValidationMessage {

    // Payment Status
    PAYER_AND_PAYEE_CANNOT_BE_NULL("validation.payer.and.payee.cannot.null"),
    PAYER_AND_PAYEE_MUST_BE_DIFFERENT("validation.payer.and.payee.must.different"),
    PAYMENT_AMOUNT_GREATER_THAN_ZERO("validation.payment.amount.greater.than.zero"),
    DAILY_LIMIT_EXCEEDED("validation.daily.limit.exceeded"),

    // Kafka Event
    FAILED_PUBLISH_SEND_EVENT("valitaion.failed.publish.send.event"),
    PAYMENT_NOT_FOUND("validation.payment.not-found"),

    // Internal error
    INTERNAL_ERROR("validation.internal.error");

    private final String key;

    ValidationMessage(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
