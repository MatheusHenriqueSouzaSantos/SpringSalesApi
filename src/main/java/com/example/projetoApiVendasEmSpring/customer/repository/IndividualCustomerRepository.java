package com.example.projetoApiVendasEmSpring.customer.repository;

import com.example.projetoApiVendasEmSpring.customer.entity.IndividualCustomer;
import com.example.projetoApiVendasEmSpring.repositories.BaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface IndividualCustomerRepository extends BaseRepository<IndividualCustomer, UUID> {
    Optional<IndividualCustomer> findByCpf(String cpf);

    Optional<IndividualCustomer> findByCpfAndActiveTrue(String cpf);

    Optional<IndividualCustomer> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);
}
