package com.example.projetoApiVendasEmSpring.repositories;

import com.example.projetoApiVendasEmSpring.entities.Installment;

import java.util.List;
import java.util.UUID;

public interface InstallmentRepository extends BaseRepository<Installment, UUID>{
    List<Installment> findByFinancialTransactionIdAndActiveTrue(UUID financialTransactionId);
    List<Installment> findByFinancialTransactionIdAndPaidTrueAndActiveTrue(UUID financialTransactionId);
    List<Installment> findByFinancialTransactionIdAndPaidFalseAndActiveTrue(UUID financialTransactionId);

}
