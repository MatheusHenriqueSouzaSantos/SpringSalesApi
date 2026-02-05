package com.example.projetoApiVendasEmSpring.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.*;

@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@DiscriminatorValue("INDIVIDUAL_CUSTOMER")
public class CorporateCustomer extends Customer{
    @Setter
    @Column(name = "legal_name", nullable = false, length = 150)
    private String legalName;
    @Setter
    @Column(name = "trade_name", nullable = true, length = 70)
    private String tradeName;
    @Setter
    @Column(name = "state_registration", nullable = true, length = 15)
    private String stateRegistration;
    @Column(name = "cnpj", nullable = false,updatable = false,length = 14)
    private String cnpj;

    public CorporateCustomer(AppUser createdBy, String email, String phone, String legalName, String tradeName, String stateRegistration, String cnpj) {
        super(createdBy, email, phone);
        this.legalName = legalName;
        this.tradeName = tradeName;
        this.stateRegistration = stateRegistration;
        this.cnpj = cnpj;
    }
}
