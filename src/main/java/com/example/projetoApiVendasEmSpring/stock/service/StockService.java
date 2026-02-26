package com.example.projetoApiVendasEmSpring.stock.service;

import com.example.projetoApiVendasEmSpring.stock.dto.StockInputDto;
import com.example.projetoApiVendasEmSpring.stock.dto.StockOutputDto;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;

import java.util.UUID;

public interface StockService {

    StockOutputDto findById(UUID id);

    StockOutputDto findByProductId(UUID productId);

    StockOutputDto increaseQuantity(UUID id, StockInputDto dto, UserDetailsImpl loggedUser);

    StockOutputDto decreaseQuantity(UUID id, StockInputDto dto, UserDetailsImpl loggedUser);
}
