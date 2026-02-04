package com.example.projetoApiVendasEmSpring.dtos.installment;

import com.example.projetoApiVendasEmSpring.dtos.appUser.AppUserAuditDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record InstallmentOutputDto(
        UUID id,
        Instant createdAt,
        AppUserAuditDto createdBy,
        Instant updatedAt,
        AppUserAuditDto updatedBy,
        boolean active,
        BigDecimal installmentAmount,
        LocalDate dueDate,
        boolean paid
) {
}
