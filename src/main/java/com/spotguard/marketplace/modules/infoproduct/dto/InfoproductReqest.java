package com.spotguard.marketplace.modules.infoproduct.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.spotguard.marketplace.core.enums.InfoproductStatus;

public record InfoproductReqest(
    @NotBlank(message = "Título é obrigatório")
    String title,

    String description,

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    BigDecimal price,

    String fileUrl,

    InfoproductStatus status
) 
{ }
