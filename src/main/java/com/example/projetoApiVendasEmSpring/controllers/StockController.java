package com.example.projetoApiVendasEmSpring.controllers;

import com.example.projetoApiVendasEmSpring.dtos.stock.StockInputDto;
import com.example.projetoApiVendasEmSpring.dtos.stock.StockOutputDto;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;
import com.example.projetoApiVendasEmSpring.services.stock.StockService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/stocks")
public class StockController {

    private final StockService service;

    public StockController(StockService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockOutputDto> getById(@PathVariable UUID id){
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/get-by-product-id/{id}")
    public ResponseEntity<StockOutputDto> getByProductId(@PathVariable UUID id){
        return ResponseEntity.ok(service.findByProductId(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/increase/{id}")
    public ResponseEntity<StockOutputDto> increase(@PathVariable UUID id, @RequestBody @Valid StockInputDto dto, @AuthenticationPrincipal UserDetailsImpl loggedUser){
        return ResponseEntity.ok(service.increaseQuantity(id,dto,loggedUser));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/decrease/{id}")
    public ResponseEntity<StockOutputDto> decrease(@PathVariable UUID id,@RequestBody @Valid StockInputDto dto, @AuthenticationPrincipal UserDetailsImpl loggedUser){
        return ResponseEntity.ok(service.decreaseQuantity(id,dto,loggedUser));
    }
}
