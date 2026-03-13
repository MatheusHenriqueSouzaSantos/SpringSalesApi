package com.example.projetoApiVendasEmSpring.customer.entity;

import com.example.projetoApiVendasEmSpring.appUser.entity.AppUser;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "individual_customer")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("INDIVIDUAL_CUSTOMER")
@Setter
public class IndividualCustomer extends Customer {
    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;
    @Column(name = "cpf", nullable = false, length = 11)
    private String cpf;

    public IndividualCustomer(AppUser createdBy, String email, String phone, String fullName, String cpf) {
        super(createdBy, email, phone);
        this.fullName = fullName;
        this.cpf = cpf;
    }
}
