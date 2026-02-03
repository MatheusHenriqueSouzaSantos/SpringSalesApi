package com.example.projetoApiVendasEmSpring.dtos.seller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SellerCreateDto(
        @NotBlank(message = "full name must not be blank")
        @Size(min = 1,max = 150,message = "the full name must contain between 1 and 150 characters ")
        String fullName,
        @NotBlank(message = "full cpf must not be blank")
        @Size(min = 11, max = 11, message = "the cpf must contain 11 characters")
        @Pattern(regexp = "\\d+",message = "the cpf must contain only numbers")
        String cpf,
        @NotBlank(message = "email must not be blank")
        @Email(message = "the email must be in valid format")
        @Size(min = 1,max = 200,message = "the email must contain between 1 and 200 characters")
        String email,
        @NotBlank(message = "phone must not be blank")
        @Size(min = 1,max = 25,message = "the phone must contain between 1 and 25 characters ")
        String phone
) {
}
