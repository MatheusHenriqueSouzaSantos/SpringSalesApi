package com.example.projetoApiVendasEmSpring.stock.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockInputDto(
        @NotNull
        @Positive(message = "the quantity must be greater than zero")
        int quantity
) {
}
