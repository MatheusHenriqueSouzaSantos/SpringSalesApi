package com.example.projetoApiVendasEmSpring.dtos.seller;

import com.example.projetoApiVendasEmSpring.dtos.appUser.AuditAppUserDto;

import java.time.Instant;
import java.util.UUID;

public record SellerOutputDto(
        UUID id,
        Instant createdAt,
        AuditAppUserDto createdBy,
        Instant updatedAt,
        AuditAppUserDto updatedBy,
        boolean active,
        String fullName,
        String cpf,
        String email,
        String phone
) {
}
