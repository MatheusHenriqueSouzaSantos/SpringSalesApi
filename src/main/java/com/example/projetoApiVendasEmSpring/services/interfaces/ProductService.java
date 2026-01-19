package com.example.projetoApiVendasEmSpring.services.interfaces;

import com.example.projetoApiVendasEmSpring.dtos.product.ProductCreateDto;
import com.example.projetoApiVendasEmSpring.dtos.product.ProductOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.product.ProductUpdateDto;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<ProductOutputDto> getAllProduct();

    ProductOutputDto getProductById(UUID id);

    ProductOutputDto createProduct(ProductCreateDto dto, UserDetailsImpl loggedUser);

    ProductOutputDto updateProduct(ProductUpdateDto dto, UserDetailsImpl loggedUser);

    void deActivateProductById(UUID id, UserDetailsImpl loggedUser);

    void reActivateProductById(UUID id, UserDetailsImpl loggedUser);
}
