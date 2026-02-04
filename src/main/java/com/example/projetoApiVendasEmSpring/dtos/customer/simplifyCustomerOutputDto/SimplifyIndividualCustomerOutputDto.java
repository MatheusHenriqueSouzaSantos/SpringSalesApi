package com.example.projetoApiVendasEmSpring.dtos.customer.simplifyCustomerOutputDto;

import com.example.projetoApiVendasEmSpring.dtos.customer.CustomerType;

import java.util.UUID;

public record SimplifyIndividualCustomerOutputDto(
        CustomerType type,
        UUID id,
        String fullName,
        String cpf
) implements SimplifyCustomerOutputDto {
}
