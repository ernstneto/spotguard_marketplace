package com.spotguard.marketplace.modules.space.service;

import com.spotguard.marketplace.core.exception.ResourceNotFoundException;
import com.spotguard.marketplace.modules.space.dto.BookingRequest;
import com.spotguard.marketplace.modules.space.dto.BookingResponse;
import com.spotguard.marketplace.modules.space.entity.Booking;
import com.spotguard.marketplace.modules.space.entity.Space;
import com.spotguard.marketplace.modules.space.entity.User;
import com.spotguard.marketplace.modules.space.repository.BookingRepository;
import com.spotguard.marketplace.modules.space.repository.SpaceRepository;
import com.spotguard.marketplace.modules.space.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SpaceRepository spaceRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository,
                          SpaceRepository spaceRepository,
                          UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.spaceRepository = spaceRepository;
        this.userRepository = userRepository;
    }

    public List<BookingResponse> findByTenant(String tenantEmail) {
        User tenant = userRepository.findByEmail(tenantEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", tenantEmail));

        return bookingRepository.findByTenantId(tenant.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public BookingResponse findById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));
        return toResponse(booking);
    }

    @Transactional
    public BookingResponse create(BookingRequest request, String tenantEmail) {
        // 1. Validar datas
        if (request.endDate().isBefore(request.startDate())) {
            throw new IllegalArgumentException("Data de fim não pode ser antes da data de início");
        }

        // 2. Buscar espaço
        Space space = spaceRepository.findById(request.spaceId())
                .orElseThrow(() -> new ResourceNotFoundException("Space", "id", request.spaceId()));

        if (space.getStatus() != Space.SpaceStatus.AVAILABLE) {
            throw new IllegalStateException("Este espaço não está disponível para reserva");
        }

        // 3. Buscar locatário
        User tenant = userRepository.findByEmail(tenantEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", tenantEmail));

        // 4. Verificar sobreposição de datas (prevenção de overbooking)
        if (bookingRepository.hasOverlappingBooking(
                request.spaceId(), request.startDate(), request.endDate())) {
            throw new IllegalStateException(
                    "Este espaço já está reservado no período solicitado");
        }

        // 5. Calcular preço total
        long days = ChronoUnit.DAYS.between(request.startDate(), request.endDate()) + 1;
        BigDecimal totalPrice = space.getPricePerDay().multiply(BigDecimal.valueOf(days));

        // 6. Criar reserva
        Booking booking = Booking.builder()
                .space(space)
                .tenant(tenant)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .totalPrice(totalPrice)
                .status(Booking.BookingStatus.PENDING)
                .build();

        return toResponse(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponse cancel(Long id, String authenticatedEmail) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));

        // Só o locatário da reserva ou admin pode cancelar
        if (!booking.getTenant().getEmail().equals(authenticatedEmail)) {
            throw new RuntimeException("Você não tem permissão para cancelar esta reserva");
        }

        if (booking.getStatus() == Booking.BookingStatus.ACTIVE) {
            throw new IllegalStateException("Não é possível cancelar uma reserva em andamento");
        }

        if (booking.getStatus() == Booking.BookingStatus.COMPLETED) {
            throw new IllegalStateException("Não é possível cancelar uma reserva já concluída");
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        return toResponse(bookingRepository.save(booking));
    }

    private BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getSpace().getId(),
                booking.getSpace().getTitle(),
                booking.getTenant().getId(),
                booking.getTenant().getName(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getTotalPrice(),
                booking.getStatus()
        );
    }
}
