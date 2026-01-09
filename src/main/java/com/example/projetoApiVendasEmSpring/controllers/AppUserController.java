package com.example.projetoApiVendasEmSpring.controllers;

import com.example.projetoApiVendasEmSpring.entities.AppUser;
import com.example.projetoApiVendasEmSpring.services.interfaces.AppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class AppUserController {

    private final AppUserService service;

    public AppUserController(AppUserService service){
        this.service=service;
    }

    @GetMapping
    public ResponseEntity<List<AppUser>> getAllUsers(){
        return ResponseEntity.ok(service.getAllAppUsers());
    }
}
