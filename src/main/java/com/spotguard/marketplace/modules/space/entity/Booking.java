package com.spotguard.marketplace.modules.space.entity;

import com.spotguard.marketplace.core.entity.BaseEntity;
import com.spotguard.marketplace.core.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private User tenant;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    public enum BookingStatus {
        /** Aguardando pagamento. */
        PENDING,

        /** Pagamento confirmado, reserva ativa. */
        CONFIRMED,

        /** Período de locação em andamento. */
        ACTIVE,

        /** Locação finalizada. */
        COMPLETED,

        /** Cancelada (reembolso aplicável). */
        CANCELLED
    }
}
