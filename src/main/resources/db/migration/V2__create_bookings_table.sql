-- =============================================
-- SpotGuard Marketplace - Tabela de Reservas
-- =============================================

CREATE TABLE bookings (
    id              BIGSERIAL PRIMARY KEY,
    space_id        BIGINT NOT NULL REFERENCES spaces(id),
    tenant_id       BIGINT NOT NULL REFERENCES users(id),
    start_date      DATE NOT NULL,
    end_date        DATE NOT NULL,
    total_price     DECIMAL(10, 2) NOT NULL CHECK (total_price >= 0),
    status          VARCHAR(20) NOT NULL DEFAULT 'PENDING'
                    CHECK (status IN ('PENDING', 'CONFIRMED', 'ACTIVE', 'COMPLETED', 'CANCELLED')),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Garante que end_date >= start_date
    CONSTRAINT chk_booking_dates CHECK (end_date >= start_date)
);

CREATE INDEX idx_bookings_space ON bookings(space_id);
CREATE INDEX idx_bookings_tenant ON bookings(tenant_id);
CREATE INDEX idx_bookings_status ON bookings(status);
CREATE INDEX idx_bookings_dates ON bookings(start_date, end_date);
