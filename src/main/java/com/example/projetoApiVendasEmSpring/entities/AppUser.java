package com.example.projetoApiVendasEmSpring.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "app_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//if user is system dont show
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

    public AppUser(AppUser createdBy, String fullName, String email, String passwordHash) {
        super(createdBy);
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
    }
}
