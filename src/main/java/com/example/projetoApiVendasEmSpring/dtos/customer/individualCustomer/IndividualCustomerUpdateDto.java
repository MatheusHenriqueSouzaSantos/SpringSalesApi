package com.example.projetoApiVendasEmSpring.dtos.customer.individualCustomer;

import com.example.projetoApiVendasEmSpring.dtos.customer.address.AddressInputDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record IndividualCustomerUpdateDto(
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
        String fullName
) {
}
