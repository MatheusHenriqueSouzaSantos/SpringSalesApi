package com.example.projetoApiVendasEmSpring.controllers;

import com.example.projetoApiVendasEmSpring.dtos.appUser.AppUserInputDto;
import com.example.projetoApiVendasEmSpring.dtos.appUser.AppUserOutputDto;
import com.example.projetoApiVendasEmSpring.entities.AppUser;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;
import com.example.projetoApiVendasEmSpring.services.interfaces.AppUserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class AppUserController {

    private final AppUserService service;

    public AppUserController(AppUserService service){
        this.service=service;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AppUserOutputDto>> getAllUsers(){
        return ResponseEntity.ok(service.getAllAppUsers());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<AppUserOutputDto> getUserById(@PathVariable UUID id){
        return ResponseEntity.ok(service.getAppUserById(id));
    }
    @GetMapping("/me")
    public ResponseEntity<AppUserOutputDto> getUserMe(@AuthenticationPrincipal UserDetailsImpl loggedUser){
        return ResponseEntity.ok(service.getAppUserMe(loggedUser));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Validated
    @GetMapping("/get-by-email")
    public ResponseEntity<AppUserOutputDto> getUserByEmail(@RequestParam(name = "email",required = true) @Email(message = "email in invalid format") @NotBlank(message = "email can not be blank") String email){
        return ResponseEntity.ok(service.getAppUserByEmail(email));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AppUserOutputDto> createUser(@RequestBody AppUserInputDto dto,@AuthenticationPrincipal UserDetailsImpl user){
        AppUserOutputDto createdUserDto=service.createAppUser(dto,user);
        URI location=URI.create("/api/users/" + createdUserDto.id());
        return ResponseEntity.created(location).body(createdUserDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<AppUserOutputDto> updateUser(@PathVariable UUID id,@RequestBody AppUserInputDto dto,@AuthenticationPrincipal UserDetailsImpl loggedUser){
        return ResponseEntity.ok(service.updateAppUser(id,dto,loggedUser));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deActivate(@PathVariable UUID id,@AuthenticationPrincipal UserDetailsImpl loggedUser){
        service.deActivateAppUserById(id,loggedUser);
        return ResponseEntity.noContent().build();
    }

}
