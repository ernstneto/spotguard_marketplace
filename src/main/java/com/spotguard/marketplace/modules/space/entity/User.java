package com.spotguard.marketplace.modules.space.entity;

import com.spotguard.marketplace.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import com.spotguard.marketplace.core.enums.UserRole;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

}
