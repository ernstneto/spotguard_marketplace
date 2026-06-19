package com.spotguard.marketplace.modules.infoproduct.controller;

import com.spotguard.marketplace.modules.infoproduct.dto.InfoproductReqest;
import com.spotguard.marketplace.modules.infoproduct.dto.InfoproductResponse;
import com.spotguard.marketplace.modules.infoproduct.service.InfoproductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/infoproducts")
public class InfoproductController {

    private final InfoproductService infoproductService;

    public InfoproductController(InfoproductService infoproductService) {
        this.infoproductService = infoproductService;
    }

    @GetMapping
    public ResponseEntity<List<InfoproductResponse>> findAllPublished() {
        return ResponseEntity.ok(infoproductService.findAllPublished());
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('CREATOR', 'ADMIN')")
    public ResponseEntity<List<InfoproductResponse>> myInfoproducts(Authentication auth) {
        return ResponseEntity.ok(infoproductService.findByCreator(auth.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InfoproductResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(infoproductService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CREATOR', 'ADMIN')")
    public ResponseEntity<InfoproductResponse> create(
            @Valid @RequestBody InfoproductReqest request,
            Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(infoproductService.create(request, auth.getName()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CREATOR', 'ADMIN')")
    public ResponseEntity<InfoproductResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody InfoproductReqest request,
            Authentication auth) {
        return ResponseEntity.ok(infoproductService.update(id, request, auth.getName()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CREATOR', 'ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication auth) {
        infoproductService.delete(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}
