package com.spotguard.marketplace.modules.space.dto;

public record AuthResponse(
        String token,
        String email,
        String role
) {}
