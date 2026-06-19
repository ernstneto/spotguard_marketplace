package com.spotguard.marketplace.modules.infoproduct.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spotguard.marketplace.core.enums.InfoproductStatus;
import com.spotguard.marketplace.core.exception.ResourceNotFoundException;
import com.spotguard.marketplace.modules.infoproduct.dto.InfoproductReqest;
import com.spotguard.marketplace.modules.infoproduct.dto.InfoproductResponse;
import com.spotguard.marketplace.modules.infoproduct.entity.Infoproduct;
import com.spotguard.marketplace.modules.infoproduct.repository.InfoproductRepository;
import com.spotguard.marketplace.modules.space.entity.User;
import com.spotguard.marketplace.modules.space.repository.UserRepository;

@Service
public class InfoproductService {
    private final InfoproductRepository infoproductRepository;
    private final UserRepository userRepository;
    
    public InfoproductService(InfoproductRepository infoproductRepository, UserRepository userRepository) {
        this.infoproductRepository = infoproductRepository;
        this.userRepository = userRepository;

    }

    public List<InfoproductResponse> findAllPublished() {
        return infoproductRepository.findByStatus(InfoproductStatus.PUBLISHED)
                .stream().map(this::toResponse).toList();
    }
    
    public List<InfoproductResponse> findByCreator(String creatorEmail) {
        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return infoproductRepository.findByCreatorId(creator.getId())
                .stream().map(this::toResponse).toList();
    }

    public InfoproductResponse findById(Long id) {
        Infoproduct infoproduct = infoproductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Infoproduct", "id", id));
        return toResponse(infoproduct);
    }

    @Transactional
    public InfoproductResponse create(InfoproductReqest request, String creatorEmail) {
        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", creatorEmail));

        Infoproduct infoproduct = Infoproduct.builder()
                .title(request.title())
                .description(request.description())
                .price(request.price())
                .fileUrl(request.fileUrl())
                .status(request.status() != null ? request.status() : InfoproductStatus.DRAFT)
                .creator(creator)
                .build();

        return toResponse(infoproductRepository.save(infoproduct));
    }

    @Transactional
    public InfoproductResponse update(Long id, InfoproductReqest request, String authenticatedEmail) {
        Infoproduct infoproduct = infoproductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Infoproduct", "id", id));

        if (!infoproduct.getCreator().getEmail().equals(authenticatedEmail)) {
            throw new RuntimeException("Você não tem permissão para alterar este infoproduto");
        }

        infoproduct.setTitle(request.title());
        infoproduct.setDescription(request.description());
        infoproduct.setPrice(request.price());
        infoproduct.setFileUrl(request.fileUrl());
        if (request.status() != null) infoproduct.setStatus(request.status());

        return toResponse(infoproductRepository.save(infoproduct));
    }

    @Transactional
    public void delete(Long id, String authenticatedEmail) {
        Infoproduct infoproduct = infoproductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Infoproduct", "id", id));

        if (!infoproduct.getCreator().getEmail().equals(authenticatedEmail)) {
            throw new RuntimeException("Você não tem permissão para deletar este infoproduto");
        }

        infoproduct.setStatus(InfoproductStatus.ARCHIVED);
        infoproductRepository.save(infoproduct);
    }

    private InfoproductResponse toResponse(Infoproduct ip) {
        return new InfoproductResponse(
                ip.getId(), ip.getTitle(), ip.getDescription(),
                ip.getPrice(), ip.getFileUrl(), ip.getStatus(), ip.getCreator().getName());
    }
}
