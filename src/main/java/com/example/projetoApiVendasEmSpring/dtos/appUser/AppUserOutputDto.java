package com.example.projetoApiVendasEmSpring.dtos.appUser;

import com.example.projetoApiVendasEmSpring.entities.enums.UserRole;

import java.time.Instant;
import java.util.UUID;

public record AppUserOutputDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        AppUserAuditDto createdBy,
        AppUserAuditDto updatedBy,
        String fullName,
        String email,
        UserRole userRole,
        boolean active) {


}
