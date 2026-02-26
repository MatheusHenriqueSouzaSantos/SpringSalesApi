package com.example.projetoApiVendasEmSpring.address.dto;

import com.example.projetoApiVendasEmSpring.appUser.dto.AuditAppUserDto;
import com.example.projetoApiVendasEmSpring.address.entity.Address;

import java.time.Instant;
import java.util.UUID;

public record AddressOutputDto(
        UUID id,
        Instant createdAt,
        AuditAppUserDto createdBy,
        Instant updatedAt,
        AuditAppUserDto updatedBy,
        boolean active,
        String street,
        String streetNumber,
        String neighborhood,
        String city,
        String stateCode

) {
    public static AddressOutputDto addressEntityToAddressDto(Address address){
        AuditAppUserDto createdBy= AuditAppUserDto.appUserToAuditAppUserDto(address.getCreatedBy());
        AuditAppUserDto updatedBy= AuditAppUserDto.appUserToAuditAppUserDto(address.getUpdatedBy());
        return new AddressOutputDto(address.getId(),address.getCreatedAt(),createdBy,address.getUpdatedAt(),updatedBy,address.isActive(),
                address.getStreet(), address.getStreetNumber(),address.getNeighborhood(),address.getCity(),address.getStateCode());
    }
}
