package com.example.projetoApiVendasEmSpring.seller.controller;

import com.example.projetoApiVendasEmSpring.seller.dto.SellerCreateDto;
import com.example.projetoApiVendasEmSpring.seller.dto.SellerOutputDto;
import com.example.projetoApiVendasEmSpring.seller.dto.SellerUpdateDto;
import com.example.projetoApiVendasEmSpring.security.userDetails.UserDetailsImpl;
import com.example.projetoApiVendasEmSpring.seller.service.SellerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sellers")
public class SellerController {
    private final SellerService service;

    public SellerController(SellerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SellerOutputDto>> getAllSellers(){
        return ResponseEntity.ok(service.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<SellerOutputDto> getSellerById(@PathVariable UUID id){
        return ResponseEntity.ok(service.findSellerById(id));
    }

    @GetMapping("/active/{id}")
    public ResponseEntity<SellerOutputDto> getActiveSellerById(@PathVariable UUID id){
        return ResponseEntity.ok(service.findActiveSellerById(id));
    }

    @GetMapping("/find-by-cpf/{cpf}")
    public ResponseEntity<SellerOutputDto> findSellerByCpf(@PathVariable String cpf){
        return ResponseEntity.ok(service.findSellerByCpf(cpf));
    }

    @GetMapping("/active/find-by-cpf/{cpf}")
    public ResponseEntity<SellerOutputDto> findActiveSellerByCpf(@PathVariable String cpf){
        return ResponseEntity.ok(service.findActiveSellerByCpf(cpf));
    }

    @PostMapping
    public ResponseEntity<SellerOutputDto> createSeller(@RequestBody @Valid SellerCreateDto dto, @AuthenticationPrincipal UserDetailsImpl loggedUser){
        SellerOutputDto createdSeller =service.createSeller(dto,loggedUser);
        URI location=URI.create("api/sellers/"+ createdSeller.id());
        return ResponseEntity.created(location).body(createdSeller);
    }


    @PutMapping("/{id}")
    public ResponseEntity<SellerOutputDto> updateSeller(@PathVariable UUID id, @RequestBody @Valid SellerUpdateDto dto, @AuthenticationPrincipal UserDetailsImpl loggedUser){
        return ResponseEntity.ok(service.updateSeller(id,dto,loggedUser));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deActivateSeller(@PathVariable UUID id, @AuthenticationPrincipal UserDetailsImpl loggedUser ){
        service.deActivateSeller(id,loggedUser);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/reactivate/{id}")
    public ResponseEntity<Void> reActivateSeller(@PathVariable UUID id, @AuthenticationPrincipal UserDetailsImpl loggedUser) {
        service.reActivateSeller(id, loggedUser);
        return ResponseEntity.noContent().build();
    }
}
