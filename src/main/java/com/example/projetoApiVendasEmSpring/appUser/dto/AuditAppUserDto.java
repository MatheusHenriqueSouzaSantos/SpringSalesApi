package com.example.projetoApiVendasEmSpring.appUser.dto;

import com.example.projetoApiVendasEmSpring.appUser.entity.AppUser;

public record AuditAppUserDto(String fullName, String email) {

    public static AuditAppUserDto appUserToAuditAppUserDto(AppUser user){
        if(user==null){
            return null;
        }
        return new AuditAppUserDto(user.getFullName(), user.getEmail());
    }
}
