package com.spotguard.marketplace.modules.infoproduct.entity;

import com.spotguard.marketplace.core.entity.BaseEntity;
import com.spotguard.marketplace.modules.space.entity.User;
import jakarta.persistence.*;
import lombok.*;
import com.spotguard.marketplace.core.enums.InfoproductStatus;

import java.math.BigDecimal;

@Entity
@Table(name = "infoproducts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Infoproduct extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "file_url")
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InfoproductStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

}
