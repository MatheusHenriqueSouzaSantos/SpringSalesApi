package com.example.projetoApiVendasEmSpring.customer.service;

import com.example.projetoApiVendasEmSpring.customer.dto.customerOutputDto.CorporateCustomerOutputDto;
import com.example.projetoApiVendasEmSpring.customer.dto.customerOutputDto.CustomerOutPutDto;
import com.example.projetoApiVendasEmSpring.customer.dto.customerOutputDto.IndividualCustomerOutputDto;
import com.example.projetoApiVendasEmSpring.customer.dto.corporateCustomerInput.CorporateCustomerInputDto;
import com.example.projetoApiVendasEmSpring.customer.dto.individualCustomerInput.IndividualCustomerInputDto;
import com.example.projetoApiVendasEmSpring.security.userDetails.UserDetailsImpl;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    //the actives methods are used to search at the order moment
    List<CustomerOutPutDto> findAll();

    CustomerOutPutDto findCustomerById(UUID id);

    CustomerOutPutDto findActiveCustomerById(UUID id);

    IndividualCustomerOutputDto findCustomerByCpf(String cpf);

    IndividualCustomerOutputDto findActiveCustomerByCpf(String cpf);

    CorporateCustomerOutputDto findCustomerByCnpj(String cnpj);

    CorporateCustomerOutputDto findActiveCustomerByCnpj(String cnpj);

    IndividualCustomerOutputDto createIndividualCustomer(IndividualCustomerInputDto dto, UserDetailsImpl loggedUser);

    CorporateCustomerOutputDto createCorporateCustomer(CorporateCustomerInputDto dto, UserDetailsImpl loggedUser);

    IndividualCustomerOutputDto updateIndividualCustomer(UUID id,IndividualCustomerInputDto dto, UserDetailsImpl loggedUser);

    CorporateCustomerOutputDto updatedCorporateCustomer(UUID id,CorporateCustomerInputDto dto, UserDetailsImpl loggedUser);

    void deActivateCustomer(UUID id, UserDetailsImpl loggedUser);

    void reActivateCustomer(UUID id, UserDetailsImpl loggedUser);
}
