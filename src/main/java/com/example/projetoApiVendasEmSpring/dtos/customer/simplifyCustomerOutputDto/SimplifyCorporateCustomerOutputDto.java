package com.example.projetoApiVendasEmSpring.dtos.customer.simplifyCustomerOutputDto;

import com.example.projetoApiVendasEmSpring.dtos.customer.CustomerType;

import java.util.UUID;

public record SimplifyCorporateCustomerOutputDto (
        CustomerType type,
        UUID id,
        String legalName,
        String cnpj
) implements SimplifyCustomerOutputDto {
}
