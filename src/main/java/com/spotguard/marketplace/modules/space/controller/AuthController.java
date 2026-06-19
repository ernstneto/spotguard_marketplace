package com.spotguard.marketplace.modules.space.controller;

import com.spotguard.marketplace.config.JwtService;
import com.spotguard.marketplace.modules.space.dto.*;
import com.spotguard.marketplace.modules.space.entity.User;
import com.spotguard.marketplace.modules.space.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
            // retorna HTTP 409 com mensagem de erro
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password())) // criptografa a senha
                .role(request.role())
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(token, user.getEmail(), user.getRole().name()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(), request.password()));

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(
                new AuthResponse(token, user.getEmail(), user.getRole().name()));
    }
}
