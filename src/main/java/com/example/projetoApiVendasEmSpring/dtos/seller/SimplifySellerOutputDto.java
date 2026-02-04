package com.example.projetoApiVendasEmSpring.dtos.seller;

import java.util.UUID;

public record SimplifySellerOutputDto(
        UUID id,
        String fullName,
        String cpf
) {
}
