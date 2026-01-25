package com.example.projetoApiVendasEmSpring.dtos.customer.address;

import com.example.projetoApiVendasEmSpring.dtos.appUser.AppUserAuditDto;
import com.example.projetoApiVendasEmSpring.entities.Address;

import java.time.Instant;
import java.util.UUID;

public record AddressOutputDto(
        UUID id,
        Instant createdAt,
        AppUserAuditDto createdBy,
        Instant updatedAt,
        AppUserAuditDto updatedBy,
        boolean active,
        String street,
        String streetNumber,
        String neighborhood,
        String city,
        String stateCode

) {
    public static AddressOutputDto addressEntityToAddressDto(Address address){
        AppUserAuditDto createdBy=AppUserAuditDto.appUserToAuditAppUserDto(address.getCreatedBy());
        AppUserAuditDto updatedBy=AppUserAuditDto.appUserToAuditAppUserDto(address.getUpdatedBy());
        return new AddressOutputDto(address.getId(),address.getCreatedAt(),createdBy,address.getUpdatedAt(),updatedBy,address.isActive(),
                address.getStreet(), address.getStreetNumber(),address.getNeighborhood(),address.getCity(),address.getStateCode());
    }
}
