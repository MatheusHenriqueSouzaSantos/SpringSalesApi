package com.example.projetoApiVendasEmSpring.financialTransaction.service;

import com.example.projetoApiVendasEmSpring.financialTransaction.dto.FinancialTransactionOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.installment.InstallmentOutputDto;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;

import java.util.List;
import java.util.UUID;

public interface FinancialTransactionService {
    FinancialTransactionOutputDto findById(UUID id);

    List<InstallmentOutputDto> payInstallment(UUID financialTransactionId, UserDetailsImpl loggedUser);
}
