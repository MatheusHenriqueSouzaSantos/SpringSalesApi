package com.example.projetoApiVendasEmSpring.financialTransaction.dto;

import com.example.projetoApiVendasEmSpring.appUser.dto.AuditAppUserDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record InstallmentOutputDto(
        UUID id,
        Instant createdAt,
        AuditAppUserDto createdBy,
        Instant updatedAt,
        AuditAppUserDto updatedBy,
        boolean active,
        BigDecimal installmentAmount,
        LocalDate dueDate,
        boolean overdue,
        boolean paid
) {
}
