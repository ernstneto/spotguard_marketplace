package com.spotguard.marketplace.modules.space.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Parâmetro de busca por proximidade.
 * A localização aqui é do USUÁRIO (GPS do dispositivo),
 * não do espaço. Por isso não é persistida — é só um filtro de busca.
 */
public record NearbySearchRequest(
    @NotNull(message = "Latitude é obrigatória")
    @Min(value = -90, message = "Latitude deve ser entre -90 e 90")
    @Max(value = 90, message = "Latitude deve ser entre -90 e 90")
    Double latitude,

    @NotNull(message = "Longitude é obrigatória")
    @Min(value = -180, message = "Longitude deve ser entre -180 e 180")
    @Max(value = 180, message = "Longitude deve ser entre -180 e 180")
    Double longitude,

    @Min(value = 100, message = "Raio mínimo: 100m")
    @Max(value = 50000, message = "Raio máximo: 50km")
    Double radiusInMeters
) {}
