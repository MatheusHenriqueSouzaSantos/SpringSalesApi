package com.example.projetoApiVendasEmSpring.controllers;

import com.example.projetoApiVendasEmSpring.dtos.product.ProductCreateDto;
import com.example.projetoApiVendasEmSpring.dtos.product.ProductOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.product.ProductUpdateDto;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;
import com.example.projetoApiVendasEmSpring.services.interfaces.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@Validated
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProductOutputDto>> getAllProducts(){
        return ResponseEntity.ok(service.findAllProduct());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductOutputDto> getProductById(@PathVariable UUID id){
        return ResponseEntity.ok(service.findProductById(id));
    }

    @GetMapping("/active/{id}")
    public ResponseEntity<ProductOutputDto> getActiveProductById(@PathVariable UUID id){
        return ResponseEntity.ok(service.findActiveProductById(id));
    }


    @GetMapping("/get-product-by-sku/{sku}")
    public ResponseEntity<ProductOutputDto> getProductBySku(
            @PathVariable @Size(min = 6, max = 6, message = "sku must contains six characters") String sku){
        return ResponseEntity.ok(service.findProductBySku(sku));
    }

    @GetMapping("/active/get-product-by-sku/{sku}")
    public ResponseEntity<ProductOutputDto> getActiveProductBySku(
            @PathVariable @Size(min = 6, max = 6, message = "sku must contains six characters") String sku){
        return ResponseEntity.ok(service.findActiveProductBySku(sku));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductOutputDto> createProduct(@RequestBody @Valid ProductCreateDto dto, @AuthenticationPrincipal UserDetailsImpl loggedUser){
        ProductOutputDto createdProduct=service.createProduct(dto,loggedUser);
        URI location=URI.create("api/products/"+ createdProduct.id());
        return ResponseEntity.created(location).body(createdProduct);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductOutputDto> updatedProduct(@PathVariable UUID id,@RequestBody @Valid ProductUpdateDto dto, @AuthenticationPrincipal UserDetailsImpl loggedUser){
        return ResponseEntity.ok(service.updateProduct(id,dto,loggedUser));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deActivateProduct(@PathVariable UUID id,@AuthenticationPrincipal UserDetailsImpl loggedUser ){
        service.deActivateProductById(id,loggedUser);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/reactivate/{id}")
    public ResponseEntity<Void> reActivateProduct(@PathVariable UUID id, @AuthenticationPrincipal UserDetailsImpl loggedUser){
        service.reActivateProductById(id,loggedUser);
        return  ResponseEntity.noContent().build();
    }
}
