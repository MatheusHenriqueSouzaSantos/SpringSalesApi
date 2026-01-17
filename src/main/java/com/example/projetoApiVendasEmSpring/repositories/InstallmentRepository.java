package com.example.projetoApiVendasEmSpring.repositories;

import com.example.projetoApiVendasEmSpring.entities.FinancialTransaction;
import com.example.projetoApiVendasEmSpring.entities.Installment;

import java.util.List;
import java.util.UUID;

public interface InstallmentRepository extends BaseRepository<Installment, UUID>{
    List<Installment> findByFinancialTransactionAndActiveTrue(FinancialTransaction financialTransaction);
    List<Installment> findByFinancialTransactionAndPaidTrueAndActiveTrue(FinancialTransaction financialTransaction);
    List<Installment> findByFinancialTransactionAndPaidFalseAndActiveTrue(FinancialTransaction financialTransaction);
}
