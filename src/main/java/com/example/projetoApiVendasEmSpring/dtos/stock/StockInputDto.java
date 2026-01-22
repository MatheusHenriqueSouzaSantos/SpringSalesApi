package com.example.projetoApiVendasEmSpring.dtos.stock;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record StockInputDto(
        @NotNull
        @Positive(message = "the quantity must be greater than zero")
        int quantity
) {
}
