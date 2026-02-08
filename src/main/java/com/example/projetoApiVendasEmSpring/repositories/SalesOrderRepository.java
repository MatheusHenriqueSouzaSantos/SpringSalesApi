package com.example.projetoApiVendasEmSpring.repositories;

import com.example.projetoApiVendasEmSpring.entities.SalesOrder;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SalesOrderRepository extends BaseRepository<SalesOrder, UUID> {
    @Query("""
        SELECT s
        FROM SalesOrder s
        ORDER BY 
                CASE s.status
                        WHEN 'OPEN' THEN 1
                        WHEN 'IN_PROGRESS' THEN 2
                        WHEN 'PAID' THEN 3
                        WHEN 'CANCELED' THEN 4
                        ELSE 5
                END            
        """)
    public List<SalesOrder> findSalesOrderOrderingByStatus();

    public Optional<SalesOrder> findByOrderCode(String orderCode);

    public Optional<SalesOrder> findByActiveTrueAndFinancialTransactionId(UUID financialTransactionId);
}
