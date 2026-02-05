package com.example.projetoApiVendasEmSpring.repositories;

import com.example.projetoApiVendasEmSpring.entities.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerRepository extends BaseRepository<Customer, UUID>{
    public List<Customer> findAllByOrderByActiveDesc();
}
