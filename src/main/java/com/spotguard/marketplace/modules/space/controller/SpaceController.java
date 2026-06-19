package com.spotguard.marketplace.modules.space.controller;

import com.spotguard.marketplace.modules.space.dto.NearbySearchRequest;
import com.spotguard.marketplace.modules.space.dto.SpaceRequest;
import com.spotguard.marketplace.modules.space.dto.SpaceResponse;
import com.spotguard.marketplace.modules.space.service.SpaceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/spaces")
public class SpaceController {

    private final SpaceService spaceService;

    public SpaceController(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    @GetMapping
    public ResponseEntity<List<SpaceResponse>> findAll() {
        return ResponseEntity.ok(spaceService.findAllAvailable());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(spaceService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")   // verifica permissão
    public ResponseEntity<SpaceResponse> create(
            @Valid @RequestBody SpaceRequest request,
            Authentication authentication) {         // autenticação
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(spaceService.create(request, authentication.getName())); // autenticação
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<SpaceResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SpaceRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(spaceService.update(id, request, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication) {
        spaceService.delete(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<SpaceResponse>> findNearby(
            @Valid NearbySearchRequest search) {
        double radius = search.radiusInMeters() != null ? search.radiusInMeters() : 5000;
        return ResponseEntity.ok(
                spaceService.findNearby(search.latitude(), search.longitude(), radius));
    }
}
