package com.example.projetoApiVendasEmSpring.dtos.salesOrderItem;

import com.example.projetoApiVendasEmSpring.dtos.appUser.AppUserAuditDto;
import com.example.projetoApiVendasEmSpring.dtos.product.SimplifyProductOutputDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record SalesOrderItemOutputDto(
        UUID id,
        Instant createdAt,
        AppUserAuditDto createdBy,
        Instant updatedAt,
        AppUserAuditDto updatedBy,
        boolean active,
        SimplifyProductOutputDto product,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal discountAmount
) {
}
