package com.example.projetoApiVendasEmSpring.dtos.salesOrder;

import com.example.projetoApiVendasEmSpring.dtos.financialTransaction.FinancialTransactionInputDto;
import com.example.projetoApiVendasEmSpring.dtos.salesOrderItem.SalesOrderItemInputDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
public record SalesOrderInputDto(
        @NotNull(message = "the customerId must not be null")
        UUID customerId,
        @NotNull
        UUID sellerId,
        @NotNull(message = "the customerId must not be null")
        @Valid
        List<SalesOrderItemInputDto> salesOrderItems,
        @DecimalMin(value = "0.01",message = "The order discount amount must be greater than 0")
        BigDecimal orderDiscountAmount,
        @NotNull(message = "the customerId must not be null")
        @Valid
        FinancialTransactionInputDto financialTransaction
) {
}
