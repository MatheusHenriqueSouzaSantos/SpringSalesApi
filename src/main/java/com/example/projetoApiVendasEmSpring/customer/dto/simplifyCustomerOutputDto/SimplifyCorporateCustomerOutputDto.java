package com.example.projetoApiVendasEmSpring.customer.dto.simplifyCustomerOutputDto;

import com.example.projetoApiVendasEmSpring.customer.dto.CustomerType;
import com.example.projetoApiVendasEmSpring.customer.entity.CorporateCustomer;

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
