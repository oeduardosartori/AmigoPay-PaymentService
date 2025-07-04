package com.amigopay.payment.payment.mapper;

import com.amigopay.payment.payment.dto.PaymentContext;
import com.amigopay.payment.payment.dto.PaymentRequest;
import com.amigopay.payment.payment.dto.PaymentResponse;
import com.amigopay.payment.payment.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    static PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getPayerId(),
                payment.getPayeeId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }

    static PaymentContext toContext(PaymentRequest request) {
        return new PaymentContext(
                request.payerId(),
                request.payeeId(),
                request.amount()
        );
    }
}
