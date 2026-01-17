package com.example.projetoApiVendasEmSpring.repositories;

import com.example.projetoApiVendasEmSpring.entities.SalesOrder;
import com.example.projetoApiVendasEmSpring.entities.SalesOrderItem;

import java.util.List;
import java.util.UUID;

public interface SalesOrderItemRepository extends BaseRepository<SalesOrderItem, UUID>{
    public List<SalesOrderItem> findBySalesOrderAndActiveTrue(SalesOrder salesOrder);
}
