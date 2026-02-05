package com.example.projetoApiVendasEmSpring.dtos.customer.simplifyCustomerOutputDto;

import com.example.projetoApiVendasEmSpring.dtos.customer.CustomerType;
import com.example.projetoApiVendasEmSpring.entities.CorporateCustomer;

import java.util.UUID;

public record SimplifyCorporateCustomerOutputDto (
        CustomerType type,
        UUID id,
        String legalName,
        String cnpj
) implements SimplifyCustomerOutputDto {
    public static SimplifyCorporateCustomerOutputDto corporateCustomerEntityToSimplifyDto(CorporateCustomer customer){
        return new SimplifyCorporateCustomerOutputDto(CustomerType.CORPORATE_CUSTOMER,customer.getId(), customer.getLegalName(), customer.getCnpj());
    }
}
