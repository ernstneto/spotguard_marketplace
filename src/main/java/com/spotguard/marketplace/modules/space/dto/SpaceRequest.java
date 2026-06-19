package com.spotguard.marketplace.modules.space.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.spotguard.marketplace.core.enums.SpaceType;

import java.math.BigDecimal;

public record SpaceRequest(
        @NotBlank(message = "Título é obrigatório")
        String title,

        String description,

        @NotNull(message = "Preço por dia é obrigatório")
        @Positive(message = "Preço deve ser positivo")
        BigDecimal pricePerDay,

        @NotBlank(message = "Endereço é obrigatório")
        String address,

        Double latitude,
        Double longitude,

        @NotNull(message = "Tipo é obrigatório")
        SpaceType type
) {}
