package com.example.projetoApiVendasEmSpring.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address extends BaseEntity{
    @Column(name = "street",nullable = false)
    private String street;

    @Column(name = "street_number",nullable = false)
    private String streetNumber;

    @Column(name = "neighborhood", nullable = false)
    private String neighborhood;

    @Column(name = "state_code",nullable = false,length = 2)
    private String stateCode;

    @Column(name = "city",nullable = false)
    private String city;


    public Address(AppUser createdBy, String street, String streetNumber, String neighborhood, String stateCode, String city) {
        super(createdBy);
        this.street = street;
        this.streetNumber = streetNumber;
        this.neighborhood = neighborhood;
        this.stateCode = stateCode;
        this.city = city;
    }
}
