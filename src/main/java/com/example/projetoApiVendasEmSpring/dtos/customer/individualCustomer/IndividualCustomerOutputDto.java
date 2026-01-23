package com.example.projetoApiVendasEmSpring.dtos.customer.individualCustomer;

import com.example.projetoApiVendasEmSpring.dtos.appUser.AppUserAuditDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.address.AddressOutputDto;

import java.time.Instant;
import java.util.UUID;

public record IndividualCustomerOutputDto(
        UUID id,
        Instant createdAt,
        AppUserAuditDto createdBy,
        Instant updatedAt,
        AppUserAuditDto updatedBy,
        boolean active,
        String email,
        String phone,
        AddressOutputDto address,
        String fullName,
        String cpf
) {
}
