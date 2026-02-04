package com.example.projetoApiVendasEmSpring.dtos.salesOrder;

import com.example.projetoApiVendasEmSpring.dtos.financialTransaction.FinancialTransactionInputDto;
import com.example.projetoApiVendasEmSpring.dtos.salesOrderItem.SalesOrderIteInputDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record SalesOrderInputDto(
        UUID customerId,
        UUID sellerId,
        List<SalesOrderIteInputDto> salesOrderItems,
        BigDecimal orderDiscountAmount,
        FinancialTransactionInputDto financialTransaction
) {
}
