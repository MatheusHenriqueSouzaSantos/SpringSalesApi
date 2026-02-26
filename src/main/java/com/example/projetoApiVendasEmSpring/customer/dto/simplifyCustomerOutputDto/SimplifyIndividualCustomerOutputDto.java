package com.example.projetoApiVendasEmSpring.customer.dto.simplifyCustomerOutputDto;

import com.example.projetoApiVendasEmSpring.customer.dto.CustomerType;
import com.example.projetoApiVendasEmSpring.customer.entity.IndividualCustomer;

import java.util.UUID;

public record SimplifyIndividualCustomerOutputDto(
        CustomerType type,
        UUID id,
        String fullName,
        String cpf
) implements SimplifyCustomerOutputDto {
    public static SimplifyIndividualCustomerOutputDto individualCustomerEntityToSimplifyDto(IndividualCustomer customer){
        return new SimplifyIndividualCustomerOutputDto(CustomerType.INDIVIDUAL_CUSTOMER,customer.getId(),customer.getFullName(), customer.getCpf());
    }
}
