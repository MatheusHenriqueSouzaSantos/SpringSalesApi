package com.example.projetoApiVendasEmSpring.services.seller;

import com.example.projetoApiVendasEmSpring.dtos.seller.SellerCreateDto;
import com.example.projetoApiVendasEmSpring.dtos.seller.SellerOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.seller.SellerUpdateDto;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;

import java.util.List;
import java.util.UUID;

public interface SellerService {
    List<SellerOutputDto> findAll();

    SellerOutputDto findSellerById(UUID id);

    SellerOutputDto findActiveSellerById(UUID id);

    SellerOutputDto findSellerByCpf(String cpf);

    SellerOutputDto findActiveSellerByCpf(String cpf);

    SellerOutputDto createSeller(SellerCreateDto dto, UserDetailsImpl loggedUser);

    SellerOutputDto updateSeller(UUID id, SellerUpdateDto dto, UserDetailsImpl loggedUser);

    void deActivateSeller(UUID id, UserDetailsImpl loggedUser);

    void reActivateSeller(UUID id, UserDetailsImpl loggedUser);
}
