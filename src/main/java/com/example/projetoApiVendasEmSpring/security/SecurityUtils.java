package com.example.projetoApiVendasEmSpring.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public boolean isAdmin(){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();

        if(auth== null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken){
            return false;
        }

        return auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"));
    }
}
