package com.example.projetoApiVendasEmSpring.stock.repository;

import com.example.projetoApiVendasEmSpring.product.entity.Product;
import com.example.projetoApiVendasEmSpring.repositories.BaseRepository;
import com.example.projetoApiVendasEmSpring.stock.entity.Stock;

import java.util.Optional;
import java.util.UUID;

public interface StockRepository extends BaseRepository<Stock, UUID> {
    public Optional<Stock> findByProductAndActiveTrue(Product product);

    public Optional<Stock> findByProductId(UUID productId);

    public Optional<Stock> findByProductIdAndActiveTrue(UUID productId);
}
