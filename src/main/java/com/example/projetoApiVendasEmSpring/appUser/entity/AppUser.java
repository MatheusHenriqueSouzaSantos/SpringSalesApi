package com.example.projetoApiVendasEmSpring.appUser.entity;

import com.example.projetoApiVendasEmSpring.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "app_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppUser extends BaseEntity {
    @Column(name = "full_name", nullable = false, length = 150)
    @Setter
    private String fullName;
    @Setter
    @Column(name = "email", nullable = false,updatable = false, length = 200)
    private String email;
    @Setter
    @Column(name = "password_hash",nullable = false,length = 100)
    private String passwordHash;
    @Column(name="user_role",nullable = false,length = 30)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public AppUser(AppUser createdBy, String fullName, String email, String passwordHash, UserRole userRole) {
        super(createdBy);
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.userRole =userRole;
    }
}
