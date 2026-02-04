package com.example.projetoApiVendasEmSpring.dtos.salesOrderItem;

import java.math.BigDecimal;
import java.util.UUID;

public record SalesOrderIteInputDto(
        UUID productId,
        int quantity,
        BigDecimal discountAmount
) {
}
