package com.example.projetoApiVendasEmSpring.dtos.stock;

import java.util.UUID;

public record StockOutputDto(
        UUID id,
        UUID productId,
        int quantity
) {
}
