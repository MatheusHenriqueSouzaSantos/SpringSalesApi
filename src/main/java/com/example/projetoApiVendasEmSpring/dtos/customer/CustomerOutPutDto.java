package com.example.projetoApiVendasEmSpring.dtos.customer;

public sealed interface CustomerOutPutDto permits IndividualCustomerOutputDto, CorporateCustomerOutputDto  {
}
