package com.example.projetoApiVendasEmSpring.product.dto;

import java.util.UUID;

public record SimplifyProductOutputDto(
        UUID id,
        String name,
        String sku
) {
}
