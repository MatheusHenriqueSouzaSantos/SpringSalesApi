package com.example.projetoApiVendasEmSpring.salesOrder.dto;

import com.example.projetoApiVendasEmSpring.appUser.dto.AuditAppUserDto;
import com.example.projetoApiVendasEmSpring.customer.dto.simplifyCustomerOutputDto.SimplifyCustomerOutputDto;
import com.example.projetoApiVendasEmSpring.financialTransaction.dto.FinancialTransactionOutputDto;
import com.example.projetoApiVendasEmSpring.seller.dto.SimplifySellerOutputDto;
import com.example.projetoApiVendasEmSpring.salesOrder.entitiy.SalesOrderStatus;

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
