package com.example.projetoApiVendasEmSpring.dtos.customer.corporateCustomer;

import com.example.projetoApiVendasEmSpring.dtos.customer.address.AddressInputDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record CorporateCustomerUpdateDto(
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
        @NotBlank(message = "the legal name must not be blank")
        @Size(min=1,max = 150, message = "the legal name must contain between 1 and 150 characters")
        String legalName,
        @Size(min=1,max = 70, message = "the trade name must contain between 1 and 70 characters")
        String tradeName,
        @Size(min=1,max = 15, message = "the trade name must contain between 1 and 15 characters")
        @Pattern(regexp = "\\d+")
        String stateRegistration
) {
}
