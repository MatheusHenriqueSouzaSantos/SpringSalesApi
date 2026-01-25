package com.example.projetoApiVendasEmSpring.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "individual_customer")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IndividualCustomer extends Customer {
    @Setter
    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;
    @Column(name = "cpf", nullable = false,updatable = false, length = 11)
    private String cpf;

    public IndividualCustomer(AppUser createdBy, String email, String phone, String fullName, String cpf) {
        super(createdBy, email, phone);
        this.fullName = fullName;
        this.cpf = cpf;
    }
}
