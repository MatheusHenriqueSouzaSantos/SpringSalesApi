package com.example.projetoApiVendasEmSpring.customer.repository;

import com.example.projetoApiVendasEmSpring.customer.entity.CorporateCustomer;
import com.example.projetoApiVendasEmSpring.repositories.BaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface CorporateCustomerRepository extends BaseRepository<CorporateCustomer, UUID> {
    public Optional<CorporateCustomer> findByCnpj(String cnpj);

    public Optional<CorporateCustomer> findByCnpjAndActiveTrue(String cnpj);

    boolean existsByEmail(String email);

    boolean existsByCnpj(String cnpj);

    Optional<CorporateCustomer> findByEmail(String email);
}
