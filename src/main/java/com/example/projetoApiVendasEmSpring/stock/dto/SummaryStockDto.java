package com.example.projetoApiVendasEmSpring.stock.dto;

import java.util.UUID;

public record SummaryStockDto(
        UUID id,
        int quantity
) {

}
