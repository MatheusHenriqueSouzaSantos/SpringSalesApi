package com.example.projetoApiVendasEmSpring.seller.dto;

import java.util.UUID;

public record SimplifySellerOutputDto(
        UUID id,
        String fullName,
        String cpf
) {
}
