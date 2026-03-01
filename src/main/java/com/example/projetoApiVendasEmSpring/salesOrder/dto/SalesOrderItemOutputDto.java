package com.example.projetoApiVendasEmSpring.salesOrder.dto;

import com.example.projetoApiVendasEmSpring.appUser.dto.AuditAppUserDto;
import com.example.projetoApiVendasEmSpring.product.dto.SimplifyProductOutputDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record SalesOrderItemOutputDto(
        UUID id,
        Instant createdAt,
        AuditAppUserDto createdBy,
        Instant updatedAt,
        AuditAppUserDto updatedBy,
        boolean active,
        SimplifyProductOutputDto product,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal discountAmount,
        BigDecimal itemTotal
) {
}
