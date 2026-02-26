package com.example.projetoApiVendasEmSpring.seller.dto;

import com.example.projetoApiVendasEmSpring.appUser.dto.AuditAppUserDto;

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
