package com.example.projetoApiVendasEmSpring.dtos.customer;

import com.example.projetoApiVendasEmSpring.dtos.appUser.AuditAppUserDto;

import java.time.Instant;
import java.util.UUID;

public record AddressOutputDto(
        UUID id,
        Instant createdAt,
        AuditAppUserDto createdBy,
        Instant updatedAt,
        AuditAppUserDto updatedBy,
        boolean active,
        String street,
        String streetNumber,
        String neighborhood,
        String city,
        String stateCode

) { }
