package com.example.projetoApiVendasEmSpring;

import com.example.projetoApiVendasEmSpring.appUser.entity.AppUser;
import com.example.projetoApiVendasEmSpring.appUser.entity.UserRole;
import com.example.projetoApiVendasEmSpring.excepetions.ResourceNotFoundException;
import com.example.projetoApiVendasEmSpring.appUser.repository.AppUserRepository;
import com.example.projetoApiVendasEmSpring.services.SystemUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

@Configuration
public class InitialConfig {

    @Value("${admin-user.email}")
    private String adminEmail;

    @Value("${admin-user.password}")
    private String adminPassword;

    @Bean
    CommandLineRunner createAdminIfNotExists(AppUserRepository repository, PasswordEncoder encoder){
        return  (args)->{
            Optional<AppUser> optionalExistsUser=repository.findAppUserByEmailExceptSystemUser(SystemUser.ID,adminEmail);

            if(optionalExistsUser.isPresent()){
                if(encoder.matches(adminPassword,optionalExistsUser.get().getPasswordHash())){
                    return;
                }
                AppUser appUser=optionalExistsUser.get();
                appUser.setPasswordHash(encoder.encode(adminPassword));
                repository.save(appUser);
            }

            String userUuidString="00000000-0000-0000-0000-000000000001";
            AppUser createdBy=repository.findById(UUID.fromString(userUuidString))
                    .orElseThrow(()->new ResourceNotFoundException("User not found"));
            if(adminPassword ==null){
                throw new IllegalArgumentException("Admin password password is null");
            }
            String passwordHash=encoder.encode(adminPassword);
            AppUser adminUser=new AppUser(createdBy,"admin",adminEmail,passwordHash, UserRole.ADMIN);
            repository.save(adminUser);
        };

    }
}
