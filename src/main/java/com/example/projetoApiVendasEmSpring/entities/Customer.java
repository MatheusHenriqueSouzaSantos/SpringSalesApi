package com.example.projetoApiVendasEmSpring.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "customer")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Customer extends BaseEntity {
    @Column(name = "email", nullable = false)
    @Setter
    private String email;

    @Column(name = "phone",nullable = false)
    @Setter
    private String phone;

    @OneToOne(fetch = FetchType.LAZY)
    @Setter
    @JoinColumn(name = "address_id", nullable = false)
    private Address addressId;

    public Customer(AppUser createdBy, String email, String phone, Address addressId) {
        super(createdBy);
        this.email = email;
        this.phone = phone;
        this.addressId = addressId;
    }
}
