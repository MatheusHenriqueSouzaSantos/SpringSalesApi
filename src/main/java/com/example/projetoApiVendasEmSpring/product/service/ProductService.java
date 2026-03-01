package com.example.projetoApiVendasEmSpring.product.service;

import com.example.projetoApiVendasEmSpring.product.dto.ProductCreateDto;
import com.example.projetoApiVendasEmSpring.product.dto.ProductOutputDto;
import com.example.projetoApiVendasEmSpring.product.dto.ProductUpdateDto;
import com.example.projetoApiVendasEmSpring.security.userDetails.UserDetailsImpl;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<ProductOutputDto> findAllProduct();

    ProductOutputDto findProductById(UUID id);

    ProductOutputDto findActiveProductById(UUID id);

    ProductOutputDto findProductBySku(String sku);

    ProductOutputDto findActiveProductBySku(String sku);

    ProductOutputDto createProduct(ProductCreateDto dto, UserDetailsImpl loggedUser);

    ProductOutputDto updateProduct(UUID id,ProductUpdateDto dto, UserDetailsImpl loggedUser);

    void deActivateProductById(UUID id, UserDetailsImpl loggedUser);

    void reActivateProductById(UUID id, UserDetailsImpl loggedUser);
}
