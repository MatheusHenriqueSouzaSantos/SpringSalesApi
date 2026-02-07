package com.example.projetoApiVendasEmSpring.dtos.financialTransaction;

import com.example.projetoApiVendasEmSpring.entities.enums.FinancialPaymentMethod;
import com.example.projetoApiVendasEmSpring.entities.enums.FinancialPaymentTerm;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Date;

public record FinancialTransactionInputDto(
        @NotNull(message = "the financial payment method must not be null")
        FinancialPaymentMethod financialPaymentMethod,
        @NotNull(message = "the financial payment term must not be null")
        FinancialPaymentTerm financialPaymentTerm,
        @FutureOrPresent(message = "The first installment due date must not be before today")
        LocalDate firstInstalmentDueDate,
        @NotNull(message = "the installment count must not be null")
        @Min(value = 1, message = "the installment count must be greater or equals 1")
        int installmentCount
) {
}
