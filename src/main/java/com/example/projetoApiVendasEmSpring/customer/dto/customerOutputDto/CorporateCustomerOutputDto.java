package com.example.projetoApiVendasEmSpring.customer.dto.customerOutputDto;

import com.example.projetoApiVendasEmSpring.appUser.dto.AuditAppUserDto;
import com.example.projetoApiVendasEmSpring.customer.dto.CustomerType;
import com.example.projetoApiVendasEmSpring.address.dto.AddressOutputDto;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public final class CorporateCustomerOutputDto implements CustomerOutPutDto {
    private final CustomerType type=CustomerType.CORPORATE_CUSTOMER;
    private final UUID id;
    private final Instant createdAt;
    private final AuditAppUserDto createdBy;
    private final Instant updatedAt;
    private final AuditAppUserDto updatedBy;
    private final boolean active;
    private final String email;
    private final String phone;
    private final AddressOutputDto address;
    private final String legalName;
    private final String tradeName;
    private final String stateRegistration;
    private final String cnpj;

    public CorporateCustomerOutputDto(UUID id, Instant createdAt, AuditAppUserDto createdBy, Instant updatedAt,
                                      AuditAppUserDto updatedBy, boolean active, String email, String phone, AddressOutputDto address,
                                      String legalName, String tradeName, String stateRegistration, String cnpj) {
        this.id = id;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.active = active;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.legalName = legalName;
        this.tradeName = tradeName;
        this.stateRegistration = stateRegistration;
        this.cnpj = cnpj;
    }
}
