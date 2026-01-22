package com.example.projetoApiVendasEmSpring.controllers;

import com.example.projetoApiVendasEmSpring.dtos.product.ProductCreateDto;
import com.example.projetoApiVendasEmSpring.dtos.product.ProductOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.product.ProductUpdateDto;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;
import com.example.projetoApiVendasEmSpring.services.interfaces.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
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
    public ResponseEntity<ProductOutputDto> getProductByUd(@PathVariable UUID id){
        return  ResponseEntity.ok(service.findProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductOutputDto> createProduct(@RequestBody ProductCreateDto dto, @AuthenticationPrincipal UserDetailsImpl loggedUser){
        ProductOutputDto createdProduct=service.createProduct(dto,loggedUser);
        URI location=URI.create("api/products/"+ createdProduct.id());
        return ResponseEntity.created(location).body(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductOutputDto> updatedProduct(@PathVariable UUID id,@RequestBody ProductUpdateDto dto, @AuthenticationPrincipal UserDetailsImpl loggedUser){
        return ResponseEntity.ok(service.updateProduct(id,dto,loggedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deActivateProduct(@PathVariable UUID id,@AuthenticationPrincipal UserDetailsImpl loggedUser ){
        service.deActivateProductById(id,loggedUser);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/reactivate/{id}")
    public ResponseEntity<Void> reActivateProduct(@PathVariable UUID id, @AuthenticationPrincipal UserDetailsImpl loggedUser){
        service.reActivateProductById(id,loggedUser);
        return  ResponseEntity.noContent().build();
    }
}
