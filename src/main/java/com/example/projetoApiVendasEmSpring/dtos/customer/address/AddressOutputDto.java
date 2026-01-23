package com.example.projetoApiVendasEmSpring.dtos.customer.address;

import com.example.projetoApiVendasEmSpring.dtos.appUser.AppUserAuditDto;

import java.time.Instant;
import java.util.UUID;

public record AddressOutputDto(
        UUID id,
        Instant createdAt,
        AppUserAuditDto createdBy,
        Instant updatedAt,
        AppUserAuditDto updatedBy,
        boolean active,
        String street,
        String streetNumber,
        String neighborhood,
        String city,
        String stateCode

) { }
