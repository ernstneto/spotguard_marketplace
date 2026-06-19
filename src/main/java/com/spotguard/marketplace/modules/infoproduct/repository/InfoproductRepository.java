package com.spotguard.marketplace.modules.infoproduct.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spotguard.marketplace.core.enums.InfoproductStatus;
import com.spotguard.marketplace.modules.infoproduct.entity.Infoproduct;

public interface InfoproductRepository extends JpaRepository<Infoproduct, Long> {
    List<Infoproduct> findByStatus(InfoproductStatus status);
    List<Infoproduct> findByCreatorId(Long creatorId);
}
