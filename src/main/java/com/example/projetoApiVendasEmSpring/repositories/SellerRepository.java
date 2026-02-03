package com.example.projetoApiVendasEmSpring.repositories;

import com.example.projetoApiVendasEmSpring.entities.Seller;

import java.util.Optional;
import java.util.UUID;

public interface SellerRepository extends BaseRepository<Seller, UUID>{
    Optional<Seller> findByCpf(String cpf);

    Optional<Seller> findByCpfAndActiveTrue(String cpf);

    boolean existsByCpf(String cpf);

    Optional<Seller> findByEmail(String email);

}
