package com.spotguard.marketplace.modules.space.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BookingRequest(
        @NotNull(message = "ID do espaço é obrigatório")
        Long spaceId,

        @NotNull(message = "Data de início é obrigatória")
        @Future(message = "Data de início deve ser no futuro")
        LocalDate startDate,

        @NotNull(message = "Data de fim é obrigatória")
        @Future(message = "Data de fim deve ser no futuro")
        LocalDate endDate
) {}
