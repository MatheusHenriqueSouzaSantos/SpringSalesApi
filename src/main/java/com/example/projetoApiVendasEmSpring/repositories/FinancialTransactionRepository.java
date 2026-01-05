package com.example.projetoApiVendasEmSpring.repositories;

import com.example.projetoApiVendasEmSpring.entities.FinancialTransaction;
import com.example.projetoApiVendasEmSpring.entities.SalesOrder;

import java.util.Optional;
import java.util.UUID;

public interface FinancialTransactionRepository extends BaseRepository<FinancialTransaction, UUID>{
    Optional<FinancialTransaction> findBySalesOrderAndIsActiveTrue(SalesOrder salesOrder);
}
