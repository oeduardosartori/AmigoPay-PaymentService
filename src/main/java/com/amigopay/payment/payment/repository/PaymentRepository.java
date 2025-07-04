package com.amigopay.payment.payment.repository;

import com.amigopay.payment.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    @Query("""
        SELECT COALESCE(SUM(p.amount), 0)
        FROM Payment p
        WHERE p.payerId = :payerId
          AND p.createdAt BETWEEN :start AND :end
          AND p.status = com.amigopay.payment.payment.enums.PaymentStatus.COMPLETED
    """)
    BigDecimal sumTodayByPayer(
            @Param("payerId") UUID payerId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
