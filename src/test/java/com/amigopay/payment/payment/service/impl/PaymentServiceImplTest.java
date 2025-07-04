package com.amigopay.payment.payment.service.impl;


import com.amigopay.payment.common.enums.ValidationMessage;
import com.amigopay.payment.exception.BusinessException;
import com.amigopay.payment.messaging.publisher.PaymentEventPublisher;
import com.amigopay.payment.payment.dto.PaymentRequest;
import com.amigopay.payment.payment.dto.PaymentResponse;
import com.amigopay.payment.payment.entity.Payment;
import com.amigopay.payment.payment.mapper.PaymentMapper;
import com.amigopay.payment.payment.repository.PaymentRepository;
import com.amigopay.payment.payment.validation.CompositePaymentValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CompositePaymentValidator validator;

    @Mock
    private PaymentEventPublisher eventPublisher;

    private UUID payerId;
    private UUID payeeId;
    private BigDecimal amount;

    private PaymentRequest request;
    private Payment payment;
    private PaymentResponse expectedResponse;

    @BeforeEach
    void setUp() {
        payerId = UUID.randomUUID();
        payeeId = UUID.randomUUID();
        amount = BigDecimal.valueOf(500);

        request = new PaymentRequest(payerId, payeeId, amount);

        payment = Payment.builder()
                .id(UUID.randomUUID())
                .payerId(payerId)
                .payeeId(payeeId)
                .amount(amount)
                .createdAt(LocalDateTime.now())
                .build();

        expectedResponse = PaymentMapper.toResponse(payment);
    }

    @Test
    @DisplayName("Should create payment, persist and publish event successfully")
    void shouldCreatePaymentSuccessfully() {
        // given
        given(paymentRepository.save(any(Payment.class))).willReturn(payment);

        // when
        PaymentResponse result = paymentService.createPayment(request);

        // then
        verify(validator).validate(any());
        verify(paymentRepository).save(any(Payment.class));
        verify(eventPublisher).publishInitiated(payment);
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should validate payment before saving")
    void shouldValidatePaymentBeforeSaving() {
        // given
        given(paymentRepository.save(any(Payment.class))).willReturn(payment);

        // when
        paymentService.createPayment(request);

        // then
        verify(validator, times(1)).validate(any());
    }

    @Test
    @DisplayName("Should persist payment using repository")
    void shouldPersistPayment() {
        // given
        given(paymentRepository.save(any(Payment.class))).willReturn(payment);

        // when
        paymentService.createPayment(request);

        // then
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    @DisplayName("Should publish event after successful creation")
    void shouldPublishEventAfterCreation() {
        // given
        given(paymentRepository.save(any(Payment.class))).willReturn(payment);

        // when
        paymentService.createPayment(request);

        // then
        verify(eventPublisher, times(1)).publishInitiated(payment);
    }

    @Test
    @DisplayName("Should throw exception when validation fails")
    void shouldThrowExceptionWhenValidationFails() {
        doThrow(new BusinessException(ValidationMessage.FAILED_PUBLISH_SEND_EVENT))
                .when(validator).validate(any());

        assertThatThrownBy(() -> paymentService.createPayment(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("valitaion.failed.publish.send.event");

        verifyNoInteractions(paymentRepository);
        verifyNoInteractions(eventPublisher);
    }

    @Test
    @DisplayName("Should throw exception when event publication fails")
    void shouldThrowExceptionWhenEventPublicationFails() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        doThrow(new BusinessException(ValidationMessage.FAILED_PUBLISH_SEND_EVENT))
                .when(eventPublisher).publishInitiated(payment);

        assertThatThrownBy(() -> paymentService.createPayment(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("valitaion.failed.publish.send.event");

        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    @DisplayName("Should return correct PaymentResponse after creation")
    void shouldReturnCorrectResponse() {
        when(paymentRepository.save(any())).thenReturn(payment);

        PaymentResponse response = paymentService.createPayment(request);

        assertThat(response.id()).isEqualTo(payment.getId());
        assertThat(response.payerId()).isEqualTo(payerId);
        assertThat(response.payeeId()).isEqualTo(payeeId);
        assertThat(response.amount()).isEqualTo(amount);
        assertThat(response.createdAt()).isEqualTo(payment.getCreatedAt());
    }
}


