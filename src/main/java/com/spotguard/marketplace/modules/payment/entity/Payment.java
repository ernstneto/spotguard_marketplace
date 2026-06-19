package com.spotguard.marketplace.modules.payment.entity;

import com.spotguard.marketplace.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import com.spotguard.marketplace.core.enums.PaymentGateway;
import com.spotguard.marketplace.core.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentGateway gateway;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "space_id", nullable = false)
    private Long spaceId;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

}
