package com.example.projetoApiVendasEmSpring.salesOrder.service;

import com.example.projetoApiVendasEmSpring.salesOrder.dto.SalesOrderInputDto;
import com.example.projetoApiVendasEmSpring.salesOrder.dto.SalesOrderOutputDto;
import com.example.projetoApiVendasEmSpring.security.userDetails.UserDetailsImpl;

import java.util.List;
import java.util.UUID;

public interface SalesOrderService {
    List<SalesOrderOutputDto> findAll();
    SalesOrderOutputDto findById(UUID id);
    SalesOrderOutputDto findByOrderCode(String orderCode);
    SalesOrderOutputDto create(SalesOrderInputDto dto, UserDetailsImpl loggedUser);
    SalesOrderOutputDto update(UUID id, SalesOrderInputDto dto, UserDetailsImpl loggedUser);
    void cancelSalesOrder(UUID id, UserDetailsImpl loggedUser);
}
