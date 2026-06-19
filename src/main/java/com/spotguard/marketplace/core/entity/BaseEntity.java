package com.spotguard.marketplace.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // autoincremento ID único

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;    // data de criação

    @UpdateTimestamp
    private LocalDateTime updatedAt;    // data de atualização
}
