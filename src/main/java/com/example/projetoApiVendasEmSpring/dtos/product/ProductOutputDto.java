package com.example.projetoApiVendasEmSpring.dtos.product;

import com.example.projetoApiVendasEmSpring.dtos.appUser.AppUserAuditDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

//return stock information join with product??
public record ProductOutputDto(
        UUID id,
        Instant createdAt,
        AppUserAuditDto createdBy,
        Instant updatedAt,
        AppUserAuditDto updatedBy,
        boolean active,
        String sku,
        String name,
        String description,
        BigDecimal price,
        SummaryStockDto stock
) {
}
