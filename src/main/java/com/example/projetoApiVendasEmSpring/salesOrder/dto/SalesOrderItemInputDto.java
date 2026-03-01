package com.example.projetoApiVendasEmSpring.salesOrder.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record SalesOrderItemInputDto(
        @NotNull(message = "the product id must not be null")
        UUID productId,
        @NotNull(message = "the quantity must not be null")
        @Min(value = 1,message = "quantity must be greater than 0")
        int quantity,
        @DecimalMin(value = "0.01",message = "The order discount amount must be greater than 0")
        BigDecimal discountAmount
) {
}
