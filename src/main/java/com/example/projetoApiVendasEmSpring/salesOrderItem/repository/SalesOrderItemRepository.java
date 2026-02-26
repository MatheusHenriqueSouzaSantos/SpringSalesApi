package com.example.projetoApiVendasEmSpring.salesOrderItem.repository;

import com.example.projetoApiVendasEmSpring.repositories.BaseRepository;
import com.example.projetoApiVendasEmSpring.salesOrder.entities.SalesOrder;
import com.example.projetoApiVendasEmSpring.salesOrderItem.entity.SalesOrderItem;

import java.util.List;
import java.util.UUID;

public interface SalesOrderItemRepository extends BaseRepository<SalesOrderItem, UUID> {
    public List<SalesOrderItem> findBySalesOrderAndActiveTrue(SalesOrder salesOrder);
}
