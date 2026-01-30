package com.example.projetoApiVendasEmSpring;

import com.example.projetoApiVendasEmSpring.entities.AppUser;
import com.example.projetoApiVendasEmSpring.entities.enums.UserRole;
import com.example.projetoApiVendasEmSpring.excepetions.ResourceNotFoundException;
import com.example.projetoApiVendasEmSpring.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Configuration
public class InitialConfig {

    @Value("${admin-user.password}")
    private String password;

    @Bean
    CommandLineRunner createAdminIfNotExists(AppUserRepository repository, PasswordEncoder encoder){
        return  (args)->{
            if(repository.verifyExistenceAppUserByEmail("adminUser@local.com")){
                return;
            }
            String userUuidString="00000000-0000-0000-0000-000000000001";
            AppUser createdBy=repository.findById(UUID.fromString(userUuidString))
                    .orElseThrow(()->new ResourceNotFoundException("User not found"));
            if(password==null){
                throw new IllegalArgumentException("Admin password password is null");
            }
            String passwordHash=encoder.encode(password);
            AppUser adminUser=new AppUser(createdBy,"admin","adminUser@local.com",passwordHash, UserRole.ADMIN);
            repository.save(adminUser);
        };

    }
}
