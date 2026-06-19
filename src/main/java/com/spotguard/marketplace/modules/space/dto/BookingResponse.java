package com.spotguard.marketplace.modules.space.dto;

import com.spotguard.marketplace.modules.space.entity.Booking;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookingResponse(
        Long id,
        Long spaceId,
        String spaceTitle,
        Long tenantId,
        String tenantName,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal totalPrice,
        Booking.BookingStatus status
) {}
