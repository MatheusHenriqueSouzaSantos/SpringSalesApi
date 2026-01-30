package com.example.projetoApiVendasEmSpring.security;

import com.example.projetoApiVendasEmSpring.entities.AppUser;
import com.example.projetoApiVendasEmSpring.excepetions.ResourceNotFoundException;
import com.example.projetoApiVendasEmSpring.repositories.AppUserRepository;
import com.example.projetoApiVendasEmSpring.services.SystemUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final AppUserRepository repository;

    public UserDetailsService(AppUserRepository repository) {
        this.repository = repository;
    }

    public UserDetails loadUserByUsername(String email){
        AppUser appUser= repository.findAppUserByEmailExceptSystemUser(SystemUser.ID,email)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));
        List<GrantedAuthority> authorities= List.of(new SimpleGrantedAuthority(appUser.getUserRole().name()));

        return new UserDetailsImpl(appUser.getId(), appUser.getEmail(),
                appUser.getPasswordHash(), appUser.isActive(),authorities);
    }
}
