package com.example.projetoApiVendasEmSpring.controllers;

import com.example.projetoApiVendasEmSpring.dtos.salesOrder.SalesOrderInputDto;
import com.example.projetoApiVendasEmSpring.dtos.salesOrder.SalesOrderOutputDto;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;
import com.example.projetoApiVendasEmSpring.services.interfaces.SalesOrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sales-orders")
public class SalesOrderController {
    private final SalesOrderService service;

    public SalesOrderController(SalesOrderService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SalesOrderOutputDto>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesOrderOutputDto> findByID(@PathVariable UUID id){
        return ResponseEntity.ok(service.findById(id));
    }
    @GetMapping("/order-code/{orderCode}")
    public ResponseEntity<SalesOrderOutputDto> findByOrderCode(@PathVariable String orderCode){
        return ResponseEntity.ok(service.findByOrderCode(orderCode));
    }
    @PostMapping
    public ResponseEntity<SalesOrderOutputDto> create(@RequestBody @Valid SalesOrderInputDto dto, @AuthenticationPrincipal UserDetailsImpl loggedUser){
        return ResponseEntity.ok(service.create(dto,loggedUser));
    }
    @PutMapping("/{id}")
    public ResponseEntity<SalesOrderOutputDto> update(@PathVariable UUID id,@RequestBody @Valid SalesOrderInputDto dto,
                                                      @AuthenticationPrincipal UserDetailsImpl loggedUser){
        return ResponseEntity.ok(service.update(id,dto,loggedUser));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelSalesOrder(@PathVariable UUID id,@AuthenticationPrincipal UserDetailsImpl loggedUSer){
        service.cancelSalesOrder(id,loggedUSer);
        return ResponseEntity.noContent().build();
    }
}
