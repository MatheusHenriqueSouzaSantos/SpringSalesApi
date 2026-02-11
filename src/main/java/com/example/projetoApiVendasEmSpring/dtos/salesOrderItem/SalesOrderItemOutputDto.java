package com.example.projetoApiVendasEmSpring.dtos.salesOrderItem;

import com.example.projetoApiVendasEmSpring.dtos.appUser.AuditAppUserDto;
import com.example.projetoApiVendasEmSpring.dtos.product.SimplifyProductOutputDto;

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
