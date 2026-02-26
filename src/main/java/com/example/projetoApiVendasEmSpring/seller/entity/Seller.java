package com.example.projetoApiVendasEmSpring.seller.entity;

import com.example.projetoApiVendasEmSpring.appUser.entity.AppUser;
import com.example.projetoApiVendasEmSpring.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "seller")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Seller extends BaseEntity {
    @Setter
    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;
    @Column(name = "cpf", nullable = false,updatable = false, length = 11)
    private String cpf;
    @Setter
    @Column(name = "email", nullable = false, length = 200)
    private String email;
    @Setter
    @Column(name = "phone",nullable = false,length = 25)
    private String phone;

    public Seller(AppUser createdBy, String fullName, String cpf, String email, String phone) {
        super(createdBy);
        this.fullName = fullName;
        this.cpf = cpf;
        this.email = email;
        this.phone = phone;
    }
}
