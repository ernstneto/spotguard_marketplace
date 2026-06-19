package com.spotguard.marketplace.modules.space.dto;

import com.spotguard.marketplace.core.enums.SpaceType;
import com.spotguard.marketplace.core.enums.SpaceStatus;

import java.math.BigDecimal;

public record SpaceResponse(
        Long id,
        String title,
        String description,
        BigDecimal pricePerDay,
        String address,
        Double latitude,
        Double longitude,
        SpaceType type,
        SpaceStatus status,
        String ownerName
) {}
