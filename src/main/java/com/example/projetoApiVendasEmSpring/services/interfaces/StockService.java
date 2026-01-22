package com.example.projetoApiVendasEmSpring.services.interfaces;

import com.example.projetoApiVendasEmSpring.dtos.stock.StockInputDto;
import com.example.projetoApiVendasEmSpring.dtos.stock.StockOutputDto;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;

import java.util.UUID;

public interface StockService {

    StockOutputDto findById(UUID id);

    StockOutputDto findByProductId(UUID productId);

    StockOutputDto increaseQuantity(UUID id, StockInputDto dto, UserDetailsImpl loggedUser);

    StockOutputDto decreaseQuantity(UUID id, StockInputDto dto, UserDetailsImpl loggedUser);
}
