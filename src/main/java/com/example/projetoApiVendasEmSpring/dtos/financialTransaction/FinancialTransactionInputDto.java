package com.example.projetoApiVendasEmSpring.dtos.financialTransaction;

import com.example.projetoApiVendasEmSpring.entities.enums.FinancialPaymentMethod;
import com.example.projetoApiVendasEmSpring.entities.enums.FinancialPaymentTerm;

import java.time.LocalDate;
import java.util.Date;

public record FinancialTransactionInputDto(
        FinancialPaymentMethod financialPaymentMethod,
        FinancialPaymentTerm financialPaymentTerm,
        LocalDate firstInstalmentDueDate,
        int installmentCount
) {
}
