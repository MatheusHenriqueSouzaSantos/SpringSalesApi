package com.example.projetoApiVendasEmSpring.repositories;

import com.example.projetoApiVendasEmSpring.entities.CorporateCustomer;

import java.util.Optional;
import java.util.UUID;

public interface CorporateCustomerRepository extends BaseRepository<CorporateCustomer, UUID>{
    public Optional<CorporateCustomer> findByCnpjAndActiveTrue(String cnpj);
}
