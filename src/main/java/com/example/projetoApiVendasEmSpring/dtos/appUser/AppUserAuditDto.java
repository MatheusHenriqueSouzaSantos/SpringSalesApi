package com.example.projetoApiVendasEmSpring.dtos.appUser;

import com.example.projetoApiVendasEmSpring.entities.AppUser;

public record AppUserAuditDto(String fullName, String email) {

    public static AppUserAuditDto appUserToAuditAppUserDto(AppUser user){
        if(user==null){
            return null;
        }
        return new AppUserAuditDto(user.getFullName(), user.getEmail());
    }
}
