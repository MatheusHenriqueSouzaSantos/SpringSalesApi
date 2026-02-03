package com.example.projetoApiVendasEmSpring.dtos.seller;

import com.example.projetoApiVendasEmSpring.dtos.appUser.AppUserAuditDto;

import java.time.Instant;
import java.util.UUID;

public record SellerOutputDto(
        UUID id,
        Instant createdAt,
        AppUserAuditDto createdBy,
        Instant updatedAt,
        AppUserAuditDto updatedBy,
        boolean active,
        String fullName,
        String cpf,
        String email,
        String phone
) {
}
