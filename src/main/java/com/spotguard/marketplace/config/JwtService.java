package com.spotguard.marketplace.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${app.security.jwt.secret-key}")
    private String secretKey;

    @Value("${app.security.jwt.expiration-time}")
    private long expirationTime;

    public String generateToken(String email, String role) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(email)                 // email
                .claims(Map.of("role", role))   // role
                .issuedAt(new Date())           // data de criação
                .expiration(new Date(System.currentTimeMillis() + expirationTime)) // data de expiração
                .signWith(key)                  // assinatura HMAC-SHA256
                .compact();                     // compactação
    }
}
