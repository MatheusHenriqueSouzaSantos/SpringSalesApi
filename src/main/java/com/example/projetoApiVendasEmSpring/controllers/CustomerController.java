package com.example.projetoApiVendasEmSpring.controllers;

import com.example.projetoApiVendasEmSpring.dtos.customer.CorporateCustomerOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.CustomerOutPutDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.IndividualCustomerOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.corporateCustomer.CorporateCustomerCreateDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.corporateCustomer.CorporateCustomerUpdateDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.individualCustomer.IndividualCustomerCreateDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.individualCustomer.IndividualCustomerUpdateDto;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;
import com.example.projetoApiVendasEmSpring.services.interfaces.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {
//normal user can see inactive customers??
    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CustomerOutPutDto>> findAllCustomer(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerOutPutDto> findCustomerById(@PathVariable UUID id){
        return ResponseEntity.ok(service.findCustomerById(id));
    }

    @GetMapping("/find-by-cpf{cpf}")
    public ResponseEntity<IndividualCustomerOutputDto> findCustomerByCpf(@PathVariable String cpf){
        return ResponseEntity.ok(service.findCustomerByCpf(cpf));
    }

    @GetMapping("/find-by-cnpj{cnpj}")
    public ResponseEntity<CorporateCustomerOutputDto> findCustomerByCnpj(@PathVariable String cnpj){
        return ResponseEntity.ok(service.findCustomerByCnpj(cnpj));
    }

    @PostMapping("/individual-customer")
    public ResponseEntity<IndividualCustomerOutputDto> createIndividualCustomer(@RequestBody @Valid IndividualCustomerCreateDto dto, @AuthenticationPrincipal UserDetailsImpl loggedUser){
        IndividualCustomerOutputDto customerDto=service.createIndividualCustomer(dto,loggedUser);
        URI location=URI.create("api/customers/"+customerDto.getId());
        return ResponseEntity.created(location).body(customerDto);
    }

    @PostMapping("/corporate-customer")
    public ResponseEntity<CorporateCustomerOutputDto> createCorporateCustomer(@RequestBody @Valid CorporateCustomerCreateDto dto, @AuthenticationPrincipal UserDetailsImpl loggedUser){
        CorporateCustomerOutputDto customerDto=service.createCorporateCustomer(dto,loggedUser);
        URI location=URI.create("api/customers/"+customerDto.getId());
        return ResponseEntity.created(location).body(customerDto);
    }

    @PutMapping("/individual-customer/{id}")
    public ResponseEntity<IndividualCustomerOutputDto> updateIndividualCustomer(@PathVariable UUID id, @RequestBody @Valid IndividualCustomerUpdateDto dto,
                                                                                 @AuthenticationPrincipal UserDetailsImpl loggedUser){
        return ResponseEntity.ok(service.updateIndividualCustomer(id,dto,loggedUser));
    }

    @PutMapping("/corporate-customer/{id}")
    public ResponseEntity<CorporateCustomerOutputDto> updateCorporateCustomer(@PathVariable UUID id, @RequestBody @Valid CorporateCustomerUpdateDto dto,
                                                                                 @AuthenticationPrincipal UserDetailsImpl loggedUser){
        return ResponseEntity.ok(service.updatedCorporateCustomer(id,dto,loggedUser));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deActivateCustomer(@PathVariable UUID id,@AuthenticationPrincipal UserDetailsImpl loggedUser){
        service.deActivateCustomer(id,loggedUser);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/reactivate/{id}")
    public ResponseEntity<Void> reActivateCustomer(@PathVariable UUID id,@AuthenticationPrincipal UserDetailsImpl loggedUser){
        service.reActivateCustomer(id,loggedUser);
        return ResponseEntity.noContent().build();
    }

}
