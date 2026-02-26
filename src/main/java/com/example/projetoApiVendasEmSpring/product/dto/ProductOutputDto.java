package com.example.projetoApiVendasEmSpring.product.dto;

import com.example.projetoApiVendasEmSpring.appUser.dto.AuditAppUserDto;
import com.example.projetoApiVendasEmSpring.stock.dto.SummaryStockDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

//return stock information join with product??
public record ProductOutputDto(
        UUID id,
        Instant createdAt,
        AuditAppUserDto createdBy,
        Instant updatedAt,
        AuditAppUserDto updatedBy,
        boolean active,
        String sku,
        String name,
        String description,
        BigDecimal price,
        SummaryStockDto stock
) {
}
