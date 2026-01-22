package com.example.projetoApiVendasEmSpring.repositories;

import com.example.projetoApiVendasEmSpring.entities.Product;
import com.example.projetoApiVendasEmSpring.entities.Stock;

import java.util.Optional;
import java.util.UUID;

public interface StockRepository extends BaseRepository<Stock, UUID>{
    public Optional<Stock> findByProductAndActiveTrue(Product product);

    public Optional<Stock> findByProductId(UUID productId);

    public Optional<Stock> findByProductIdAndActiveTrue(UUID productId);
}
