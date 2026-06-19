package com.spotguard.marketplace.modules.space.controller;

import com.spotguard.marketplace.modules.space.dto.BookingRequest;
import com.spotguard.marketplace.modules.space.dto.BookingResponse;
import com.spotguard.marketplace.modules.space.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('TENANT', 'OWNER', 'ADMIN')")
    public ResponseEntity<List<BookingResponse>> myBookings(Authentication authentication) {
        return ResponseEntity.ok(bookingService.findByTenant(authentication.getName()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TENANT', 'OWNER', 'ADMIN')")
    public ResponseEntity<BookingResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TENANT', 'ADMIN')")
    public ResponseEntity<BookingResponse> create(
            @Valid @RequestBody BookingRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.create(request, authentication.getName()));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('TENANT', 'OWNER', 'ADMIN')")
    public ResponseEntity<BookingResponse> cancel(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(bookingService.cancel(id, authentication.getName()));
    }
}
