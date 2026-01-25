package com.example.projetoApiVendasEmSpring.services.interfaces;

import com.example.projetoApiVendasEmSpring.dtos.customer.CorporateCustomerOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.CustomerOutPutDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.IndividualCustomerOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.corporateCustomer.CorporateCustomerCreateDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.corporateCustomer.CorporateCustomerUpdateDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.individualCustomer.IndividualCustomerCreateDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.individualCustomer.IndividualCustomerUpdateDto;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    List<CustomerOutPutDto> findAll();

    CustomerOutPutDto findCustomerById(UUID id);

    IndividualCustomerOutputDto findCustomerByCpf(String cpf);

    CorporateCustomerOutputDto findCustomerByCnpj(String cnpj);

    IndividualCustomerOutputDto createIndividualCustomer(IndividualCustomerCreateDto dto);

    CorporateCustomerOutputDto createCorporateCustomer(CorporateCustomerCreateDto dtp);

    IndividualCustomerOutputDto updateIndividualCustomer(IndividualCustomerUpdateDto dto);

    CorporateCustomerOutputDto updatedCorporateCustomer(CorporateCustomerUpdateDto dto);

    void deActivateCustomer(UUID id);

    void reActivateCustomer(UUID id);
}
