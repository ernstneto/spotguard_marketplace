package com.spotguard.marketplace.modules.space.service;

import com.spotguard.marketplace.core.exception.ResourceNotFoundException;
import com.spotguard.marketplace.modules.space.dto.SpaceRequest;
import com.spotguard.marketplace.modules.space.dto.SpaceResponse;
import com.spotguard.marketplace.modules.space.entity.Space;
import com.spotguard.marketplace.modules.space.entity.User;
import com.spotguard.marketplace.modules.space.repository.SpaceRepository;
import com.spotguard.marketplace.modules.space.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.spotguard.marketplace.core.enums.SpaceStatus;
import org.locationtech.jts.geom.Point;

import java.util.List;

@Service
public class SpaceService {

    private final SpaceRepository spaceRepository;
    private final UserRepository userRepository;

    public SpaceService(SpaceRepository spaceRepository, UserRepository userRepository) {
        this.spaceRepository = spaceRepository;
        this.userRepository = userRepository;
    }

    public List<SpaceResponse> findAllAvailable() {
        return spaceRepository.findByStatus(SpaceStatus.AVAILABLE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public SpaceResponse findById(Long id) {
        Space space = spaceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Space", "id", id));
        return toResponse(space);
    }

    @Transactional
    public SpaceResponse create(SpaceRequest request, String ownerEmail) {
        // 1. Verifica se o dono existe
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", ownerEmail));

        // 2. Cria o ponto PostGIS via método da entidade
        Point location = Space.createLocation(request.latitude(), request.longitude());

        // 3. Cria o espaço
        Space space = Space.builder()
                .title(request.title())
                .description(request.description())
                .pricePerDay(request.pricePerDay())
                .address(request.address())
                .location(location)
                .type(request.type())
                .status(SpaceStatus.AVAILABLE)
                .owner(owner)
                .build();
        // 4. Salva o espaço
        return toResponse(spaceRepository.save(space));
    }

    @Transactional
    public SpaceResponse update(Long id, SpaceRequest request, String authenticatedEmail) {
        Space space = spaceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Space", "id", id));

        validateOwnership(space, authenticatedEmail);

        space.setTitle(request.title());
        space.setDescription(request.description());
        space.setPricePerDay(request.pricePerDay());
        space.setAddress(request.address());
        space.setType(request.type());

        if (request.latitude() != null && request.longitude() != null) {
            space.setLocation(Space.createLocation(request.latitude(), request.longitude()));
        }

        return toResponse(spaceRepository.save(space));
    }

    @Transactional
    public void delete(Long id, String authenticatedEmail) {
        Space space = spaceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Space", "id", id));

        validateOwnership(space, authenticatedEmail);

        space.setStatus(SpaceStatus.INACTIVE);
        spaceRepository.save(space);
    }

    /**
     * Verifica se o usuário autenticado é o dono do espaço.
     * Admins podem editar/deletar qualquer espaço.
     */
    private void validateOwnership(Space space, String authenticatedEmail) {
        boolean isOwner = space.getOwner().getEmail().equals(authenticatedEmail);
        // Admins bypass — verificado via @PreAuthorize no controller
        if (!isOwner) {
            throw new RuntimeException("Você não tem permissão para alterar este espaço");
        }
    }

    private SpaceResponse toResponse(Space space) {
        return new SpaceResponse(
                space.getId(),
                space.getTitle(),
                space.getDescription(),
                space.getPricePerDay(),
                space.getAddress(),
                space.getLatitude(),
                space.getLongitude(),
                space.getType(),
                space.getStatus(),
                space.getOwner().getName()
        );
    }

    public List<SpaceResponse> findNearby(double latitude, double longitude, double radiusInMeters) {
        return spaceRepository.findNearby(latitude, longitude, radiusInMeters)
                .stream()
                .map(this::toResponse)
                .toList();
    }
}
