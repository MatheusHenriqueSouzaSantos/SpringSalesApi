package com.example.projetoApiVendasEmSpring.seller.repository;

import com.example.projetoApiVendasEmSpring.repositories.BaseRepository;
import com.example.projetoApiVendasEmSpring.seller.entity.Seller;

import java.util.Optional;
import java.util.UUID;

public interface SellerRepository extends BaseRepository<Seller, UUID> {
    Optional<Seller> findByCpf(String cpf);

    Optional<Seller> findByCpfAndActiveTrue(String cpf);

    boolean existsByCpf(String cpf);

    Optional<Seller> findByEmail(String email);

}
