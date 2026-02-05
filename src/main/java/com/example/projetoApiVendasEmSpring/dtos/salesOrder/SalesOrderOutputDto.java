package com.example.projetoApiVendasEmSpring.dtos.salesOrder;

import com.example.projetoApiVendasEmSpring.dtos.appUser.AuditAppUserDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.simplifyCustomerOutputDto.SimplifyCustomerOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.financialTransaction.FinancialTransactionOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.salesOrderItem.SalesOrderItemOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.seller.SimplifySellerOutputDto;
import com.example.projetoApiVendasEmSpring.entities.enums.SalesOrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SalesOrderOutputDto(
        UUID id,
        Instant createdAt,
        AuditAppUserDto createdBy,
        Instant updatedAt,
        AuditAppUserDto updatedBy,
        boolean active,
        String orderCode,
        SalesOrderStatus status,
        SimplifyCustomerOutputDto customer,
        SimplifySellerOutputDto seller,
        List<SalesOrderItemOutputDto> salesOrderItems,
        BigDecimal subTotalAmount,
        BigDecimal orderDiscountAmount,
        BigDecimal totalAmount,
        FinancialTransactionOutputDto financialTransaction
) {
}
