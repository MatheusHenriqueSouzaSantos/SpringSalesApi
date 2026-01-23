package com.example.projetoApiVendasEmSpring.dtos.customer.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressInputDto(
        @NotBlank(message = "the street must not be blank")
        @Size(min = 1, max = 150, message = "the street must contain between 1 and 150 characters")
        String street,
        @NotBlank(message = "the street number must not be blank")
        @Size(min = 1, max = 150, message = "the street number must contain between 1 and 20 characters")
        String streetNumber,
        @NotBlank(message = "the neighborhood must not be blank")
        @Size(min = 1, max = 150, message = "the neighborhood must contain between 1 and 150 characters")
        String neighborhood,
        @NotBlank(message = "the city must not be blank")
        @Size(min = 1, max = 150, message = "the city must contain between 1 and 150 characters")
        String city,
        @NotBlank(message = "the state code must not be blank")
        @Size(min = 2, max = 2, message = "the state code must contain 2 characters")
        String stateCode
) {
}
