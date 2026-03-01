package com.example.projetoApiVendasEmSpring.financialTransaction.repository;

import com.example.projetoApiVendasEmSpring.financialTransaction.entity.Installment;
import com.example.projetoApiVendasEmSpring.common.BaseRepository;

import java.util.List;
import java.util.UUID;

public interface InstallmentRepository extends BaseRepository<Installment, UUID> {
    List<Installment> findByFinancialTransactionIdAndActiveTrue(UUID financialTransactionId);
    List<Installment> findByFinancialTransactionIdAndPaidTrueAndActiveTrue(UUID financialTransactionId);
    List<Installment> findByFinancialTransactionIdAndPaidFalseAndActiveTrue(UUID financialTransactionId);

}
