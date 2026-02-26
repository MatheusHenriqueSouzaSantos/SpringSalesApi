package com.example.projetoApiVendasEmSpring.stock.dto;

import java.util.UUID;

public record StockOutputDto(
        UUID id,
        UUID productId,
        int quantity
) {
}
