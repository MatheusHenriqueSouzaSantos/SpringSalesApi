package com.example.projetoApiVendasEmSpring.financialTransaction.repository;

import com.example.projetoApiVendasEmSpring.financialTransaction.entity.FinancialTransaction;
import com.example.projetoApiVendasEmSpring.common.BaseRepository;
import com.example.projetoApiVendasEmSpring.salesOrder.entitiy.SalesOrder;

import java.util.Optional;
import java.util.UUID;

public interface FinancialTransactionRepository extends BaseRepository<FinancialTransaction, UUID> {
    Optional<FinancialTransaction> findBySalesOrderAndActiveTrue(SalesOrder salesOrder);
}
