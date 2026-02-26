package com.example.projetoApiVendasEmSpring.product.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductCreateDto(
        @NotBlank(message = "sku can not be blank")
        @Size(min = 1, max = 85, message = "sku must contain between 1 and 85 characters")
        String sku,
        @NotBlank(message = "name can not be blank")
        @Size(min = 1,max = 150, message = "name must contain between 1 and 150 characters")
        String name,
        @Size(min = 1,max = 300, message = "description must contain between 1 and 150 characters")
        String description,
        @NotNull(message = "The price must not be null")
        @Digits(integer = 10,fraction = 2,message = "the price must have at most 10 integer and 2 fraction")
        @Positive(message = "The price must be positive")
        BigDecimal price
) {
}
