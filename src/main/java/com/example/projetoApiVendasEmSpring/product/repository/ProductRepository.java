package com.example.projetoApiVendasEmSpring.product.repository;

import com.example.projetoApiVendasEmSpring.product.entity.Product;
import com.example.projetoApiVendasEmSpring.repositories.BaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends BaseRepository<Product, UUID> {

    Optional<Product> findProductBySkuAndActiveTrue(String sku);

    Optional<Product> findProductBySku(String sku);

    boolean existsBySku(String sku);
}
