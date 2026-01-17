package com.example.projetoApiVendasEmSpring.dtos.appUser;

import com.example.projetoApiVendasEmSpring.entities.AppUser;

public record AuditAppUserDto(String fullName, String email) {

    public static AuditAppUserDto appUserToAuditAppUserDto(AppUser user){
        if(user==null){
            return null;
        }
        return new AuditAppUserDto(user.getFullName(), user.getEmail());
    }
}
