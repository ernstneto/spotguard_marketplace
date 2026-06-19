package com.spotguard.marketplace.modules.space.repository;

import com.spotguard.marketplace.modules.space.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByTenantId(Long tenantId);

    List<Booking> findBySpaceId(Long spaceId);

    /**
     * Verifica se existe sobreposição de datas para um espaço.
     * Duas reservas se sobrepõem quando:
     *   reserva1.start_date <= reserva2.end_date AND reserva1.end_date >= reserva2.start_date
     *
     * @return true se existe conflito de datas
     */
    @Query(value = """
            SELECT COUNT(b) > 0 FROM bookings b
            WHERE b.space_id = :spaceId
            AND b.status NOT IN ('CANCELLED')
            AND b.start_date <= :endDate
            AND b.end_date >= :startDate
            """, nativeQuery = true)
    boolean hasOverlappingBooking(
            @Param("spaceId") Long spaceId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * Busca reservas ativas de um espaço em um período específico.
     */
    @Query("""
            SELECT b FROM Booking b
            WHERE b.space.id = :spaceId
            AND b.status IN ('CONFIRMED', 'ACTIVE')
            AND b.startDate <= :endDate
            AND b.endDate >= :startDate
            """)
    List<Booking> findActiveBookingsInPeriod(
            @Param("spaceId") Long spaceId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
