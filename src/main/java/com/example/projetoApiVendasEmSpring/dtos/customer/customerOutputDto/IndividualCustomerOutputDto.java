package com.example.projetoApiVendasEmSpring.dtos.customer.customerOutputDto;

import com.example.projetoApiVendasEmSpring.dtos.appUser.AuditAppUserDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.CustomerType;
import com.example.projetoApiVendasEmSpring.dtos.customer.address.AddressOutputDto;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public final class IndividualCustomerOutputDto implements CustomerOutPutDto {
    private final CustomerType type=CustomerType.INDIVIDUAL_CUSTOMER;
    private final UUID id;
    private final Instant createdAt;
    private final AuditAppUserDto createdBy;
    private final Instant updatedAt;
    private final AuditAppUserDto updatedBy;
    private final boolean active;
    private final String email;
    private final String phone;
    private final AddressOutputDto address;
    private final String fullName;
    private final String cpf;

    public IndividualCustomerOutputDto(UUID id, Instant createdAt, AuditAppUserDto createdBy, Instant updatedAt, AuditAppUserDto updatedBy,
                                       boolean active, String email, String phone, AddressOutputDto address, String fullName, String cpf) {
        this.id = id;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.active = active;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.fullName = fullName;
        this.cpf = cpf;
    }
}
