package com.example.projetoApiVendasEmSpring.customer.entity;

import com.example.projetoApiVendasEmSpring.address.entity.Address;
import com.example.projetoApiVendasEmSpring.appUser.entity.AppUser;
import com.example.projetoApiVendasEmSpring.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "customer")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorColumn(name = "customer_type")
public abstract class Customer extends BaseEntity {
    @Column(name = "email", nullable = false)
    @Setter
    private String email;

    @Column(name = "phone",nullable = false)
    @Setter
    private String phone;

    @Setter
    @OneToOne(mappedBy = "customer")
    private Address address;


    public Customer(AppUser createdBy, String email, String phone) {
        super(createdBy);
        this.email = email;
        this.phone = phone;
    }
}
