package com.example.projetoApiVendasEmSpring.repositories;

import com.example.projetoApiVendasEmSpring.entities.FinancialTransaction;
import com.example.projetoApiVendasEmSpring.entities.Installment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface InstallmentRepository extends BaseRepository<Installment, UUID>{
    List<Installment> findByFinancialTransactionIdAndActiveTrue(UUID financialTransactionId);
    List<Installment> findByFinancialTransactionIdAndPaidTrueAndActiveTrue(UUID financialTransactionId);
    List<Installment> findByFinancialTransactionIdAndPaidFalseAndActiveTrue(UUID financialTransactionId);

}
