package com.example.projetoApiVendasEmSpring.services.customer;

import com.example.projetoApiVendasEmSpring.dtos.customer.customerOutputDto.CorporateCustomerOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.customerOutputDto.CustomerOutPutDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.customerOutputDto.IndividualCustomerOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.corporateCustomer.CorporateCustomerCreateDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.corporateCustomer.CorporateCustomerUpdateDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.individualCustomer.IndividualCustomerCreateDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.individualCustomer.IndividualCustomerUpdateDto;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;

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

    IndividualCustomerOutputDto createIndividualCustomer(IndividualCustomerCreateDto dto, UserDetailsImpl loggedUser);

    CorporateCustomerOutputDto createCorporateCustomer(CorporateCustomerCreateDto dto, UserDetailsImpl loggedUser);

    IndividualCustomerOutputDto updateIndividualCustomer(UUID id,IndividualCustomerUpdateDto dto, UserDetailsImpl loggedUser);

    CorporateCustomerOutputDto updatedCorporateCustomer(UUID id,CorporateCustomerUpdateDto dto, UserDetailsImpl loggedUser);

    void deActivateCustomer(UUID id, UserDetailsImpl loggedUser);

    void reActivateCustomer(UUID id, UserDetailsImpl loggedUser);
}
