package com.example.projetoApiVendasEmSpring.customer.repository;

import com.example.projetoApiVendasEmSpring.customer.entity.Customer;
import com.example.projetoApiVendasEmSpring.repositories.BaseRepository;

import java.util.List;
import java.util.UUID;

public interface CustomerRepository extends BaseRepository<Customer, UUID> {
    public List<Customer> findAllByOrderByActiveDesc();
}
