package com.example.projetoApiVendasEmSpring.services.interfaces;

import com.example.projetoApiVendasEmSpring.dtos.salesOrder.SalesOrderInputDto;
import com.example.projetoApiVendasEmSpring.dtos.salesOrder.SalesOrderOutputDto;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;

import java.util.List;
import java.util.UUID;

public interface SalesOrderService {
    List<SalesOrderOutputDto> getAll();
    SalesOrderOutputDto getById(UUID id);
    SalesOrderOutputDto getByOrderCode(String orderCode);
    SalesOrderOutputDto create(SalesOrderInputDto dto, UserDetailsImpl loggedUser);
    SalesOrderOutputDto update(UUID id, SalesOrderInputDto dto, UserDetailsImpl loggedUser);
}
