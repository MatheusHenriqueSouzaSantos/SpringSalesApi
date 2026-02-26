package com.example.projetoApiVendasEmSpring.customer.dto.individualCustomerInput;

import com.example.projetoApiVendasEmSpring.address.dto.AddressInputDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record IndividualCustomerCreateDto(
        @NotNull(message = "the address must not be null")
        @Valid
        AddressInputDto address,
        @NotBlank(message = "the email must not be blank")
        @Email(message = "the email must be a valid format")
        @Size(min = 1,max = 200, message = "the email must contain between 1 and 200 characters")
        String email,
        @NotBlank(message = "the phone must not be blank")
        @Size(min = 1,max = 25, message = "the phone must contain between 1 and 25 characters")
        String phone,
        @NotBlank(message = "the full name must not be blank")
        @Size(min = 1,max = 150, message = "the full name must contain between 1 and 150 characters")
        String fullName,
        @NotBlank(message = "the cpf must not be blank")
        @Size(min = 11, max = 11, message = "the cpf must contain 11 characters")
        @Pattern(regexp = "\\d+",message = "the cpf must contain only numbers")
        String cpf
) {
}
