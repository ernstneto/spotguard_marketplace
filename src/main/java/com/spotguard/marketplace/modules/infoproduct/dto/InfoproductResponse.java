package com.spotguard.marketplace.modules.infoproduct.dto;

import java.math.BigDecimal;

import com.spotguard.marketplace.core.enums.InfoproductStatus;

public record InfoproductResponse(
        Long id,
        String title,
        String description,
        BigDecimal price,
        String fileUrl,
        InfoproductStatus status,
        String creatorName
) {}
