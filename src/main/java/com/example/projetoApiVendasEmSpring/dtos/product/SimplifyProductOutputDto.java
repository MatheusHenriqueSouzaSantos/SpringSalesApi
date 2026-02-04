package com.example.projetoApiVendasEmSpring.dtos.product;

import java.util.UUID;

public record SimplifyProductOutputDto(
        UUID id,
        String name,
        String sku
) {
}
