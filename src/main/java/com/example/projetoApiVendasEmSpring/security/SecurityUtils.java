package com.example.projetoApiVendasEmSpring.security;

import com.example.projetoApiVendasEmSpring.entities.AppUser;
import com.example.projetoApiVendasEmSpring.excepetions.ResourceNotFoundException;
import com.example.projetoApiVendasEmSpring.repositories.AppUserRepository;
import com.example.projetoApiVendasEmSpring.services.SystemUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private AppUserRepository repository;

    public SecurityUtils(AppUserRepository repository) {
        this.repository = repository;
    }

    public boolean isAdmin(){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();

        if(auth== null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken){
            return false;
        }

        return auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean verifyUserIsActiveByEmail(String email){
        AppUser user= repository.findAppUserByEmailExceptSystemUser(SystemUser.ID,email)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));
        return user.isActive();
    }
}
