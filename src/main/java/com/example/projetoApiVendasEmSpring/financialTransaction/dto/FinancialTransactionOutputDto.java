package com.example.projetoApiVendasEmSpring.financialTransaction.dto;

import com.example.projetoApiVendasEmSpring.appUser.dto.AuditAppUserDto;
import com.example.projetoApiVendasEmSpring.dtos.installment.InstallmentOutputDto;
import com.example.projetoApiVendasEmSpring.financialTransaction.entity.FinancialPaymentMethod;
import com.example.projetoApiVendasEmSpring.financialTransaction.entity.FinancialPaymentTerm;
import com.example.projetoApiVendasEmSpring.financialTransaction.entity.FinancialTransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record FinancialTransactionOutputDto(
          UUID id,
          Instant createdAt,
          AuditAppUserDto createdBy,
          Instant updatedAt,
          AuditAppUserDto updatedBy,
          boolean active,
          FinancialTransactionStatus status,
          FinancialPaymentMethod paymentMethod,
          FinancialPaymentTerm paymentTerm,
          List<InstallmentOutputDto> installments,
          long installmentCount,
          long paidInstallmentCount,
          BigDecimal totalPaidAmount
) {
}
