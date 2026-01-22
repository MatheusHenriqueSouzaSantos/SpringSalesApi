package com.example.projetoApiVendasEmSpring.dtos.product;

import java.util.UUID;

public record SummaryStockDto(
        UUID id,
        int quantity
) {

}
