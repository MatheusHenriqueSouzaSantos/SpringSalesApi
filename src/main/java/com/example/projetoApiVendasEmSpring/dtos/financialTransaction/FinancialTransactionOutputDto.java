package com.example.projetoApiVendasEmSpring.dtos.financialTransaction;

import com.example.projetoApiVendasEmSpring.dtos.appUser.AppUserAuditDto;
import com.example.projetoApiVendasEmSpring.dtos.installment.InstallmentOutputDto;
import com.example.projetoApiVendasEmSpring.entities.enums.FinancialPaymentMethod;
import com.example.projetoApiVendasEmSpring.entities.enums.FinancialPaymentTerm;
import com.example.projetoApiVendasEmSpring.entities.enums.FinancialTransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record FinancialTransactionOutputDto(
          UUID id,
          Instant createdAt,
          AppUserAuditDto createdBy,
          Instant updatedAt,
          AppUserAuditDto updatedBy,
          boolean active,
          FinancialTransactionStatus status,
          FinancialPaymentMethod paymentMethod,
          FinancialPaymentTerm paymentTerm,
          List<InstallmentOutputDto> installments,
          int installmentCount,
          int paidInstallmentCount,
          BigDecimal totalPaidAmount
) {
}
