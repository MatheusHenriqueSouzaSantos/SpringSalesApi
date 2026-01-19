package com.example.projetoApiVendasEmSpring.dtos.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProductUpdateDto(
        @NotBlank(message = "name can not be blank")
        @Size(min = 1,max = 150, message = "name must contain between 1 and 150 characters")
        String name,
        @Size(min = 1,max = 300, message = "description must contain between 1 and 150 characters")
        String description,
        @NotBlank(message = "price can not be blank")
        @Pattern(regexp = "\\d+(\\.\\d{1,2})?", message = "price must contains only numbers, and up to 2 decimal places")
        String price
) {
}
