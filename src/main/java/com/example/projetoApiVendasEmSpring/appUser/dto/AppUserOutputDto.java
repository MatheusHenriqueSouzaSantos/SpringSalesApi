package com.example.projetoApiVendasEmSpring.appUser.dto;

import com.example.projetoApiVendasEmSpring.appUser.entity.UserRole;

import java.time.Instant;
import java.util.UUID;

public record AppUserOutputDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        AuditAppUserDto createdBy,
        AuditAppUserDto updatedBy,
        String fullName,
        String email,
        UserRole userRole,
        boolean active) {


}
