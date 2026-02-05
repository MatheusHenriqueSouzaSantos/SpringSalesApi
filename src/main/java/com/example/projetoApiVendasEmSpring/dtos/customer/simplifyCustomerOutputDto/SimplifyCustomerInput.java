package com.example.projetoApiVendasEmSpring.dtos.customer.simplifyCustomerOutputDto;

import com.example.projetoApiVendasEmSpring.dtos.customer.CustomerType;

import java.util.UUID;

public record SimplifyCustomerInput(
        CustomerType type,
        UUID uuid
) {
}
