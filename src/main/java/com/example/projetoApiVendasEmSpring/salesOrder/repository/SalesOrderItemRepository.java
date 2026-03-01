package com.example.projetoApiVendasEmSpring.salesOrder.repository;

import com.example.projetoApiVendasEmSpring.common.BaseRepository;
import com.example.projetoApiVendasEmSpring.salesOrder.entitiy.SalesOrder;
import com.example.projetoApiVendasEmSpring.salesOrder.entitiy.SalesOrderItem;

import java.util.List;
import java.util.UUID;

public interface SalesOrderItemRepository extends BaseRepository<SalesOrderItem, UUID> {
    public List<SalesOrderItem> findBySalesOrderAndActiveTrue(SalesOrder salesOrder);
}
