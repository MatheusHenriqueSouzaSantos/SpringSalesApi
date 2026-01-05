package com.example.projetoApiVendasEmSpring.repositories;

import com.example.projetoApiVendasEmSpring.entities.BaseEntity;
import com.example.projetoApiVendasEmSpring.entities.IndividualCustomer;

import java.util.Optional;
import java.util.UUID;

public interface IndividualCustomerRepository extends BaseRepository<IndividualCustomer, UUID> {
    Optional<IndividualCustomer> findByCpfAndIsActiveTrue(String cpf);
}
