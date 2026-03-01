package com.example.projetoApiVendasEmSpring.address.entity;

import com.example.projetoApiVendasEmSpring.customer.entity.Customer;
import com.example.projetoApiVendasEmSpring.appUser.entity.AppUser;
import com.example.projetoApiVendasEmSpring.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address extends BaseEntity {
    @Setter
    @Column(name = "street",nullable = false)
    private String street;
    @Setter
    @Column(name = "street_number",nullable = false)
    private String streetNumber;
    @Setter
    @Column(name = "neighborhood", nullable = false)
    private String neighborhood;
    @Setter
    @Column(name = "city",nullable = false)
    private String city;
    @Setter
    @Column(name = "state_code",nullable = false,length = 2)
    private String stateCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",nullable = false,updatable = false)
    private Customer customer;


    public Address(AppUser createdBy, String street, String streetNumber, String neighborhood, String city, String stateCode, Customer customer) {
        super(createdBy);
        this.street = street;
        this.streetNumber = streetNumber;
        this.neighborhood = neighborhood;
        this.city = city;
        this.stateCode = stateCode;
        this.customer = customer;
    }
}
