package com.example.projetoApiVendasEmSpring.repositories;

import com.example.projetoApiVendasEmSpring.entities.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends BaseRepository<Product, UUID>{

    Optional<Product> findProductBySkuAndActiveTrue(String sku);

    Optional<Product> findProductBySku(String sku);

    boolean existsBySku(String sku);
}
