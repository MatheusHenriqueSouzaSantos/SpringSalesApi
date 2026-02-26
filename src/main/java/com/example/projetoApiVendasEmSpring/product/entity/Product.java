package com.example.projetoApiVendasEmSpring.product.entity;

import com.example.projetoApiVendasEmSpring.appUser.entity.AppUser;
import com.example.projetoApiVendasEmSpring.entities.BaseEntity;
import com.example.projetoApiVendasEmSpring.stock.entity.Stock;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product extends BaseEntity {

    @Column(name = "sku", nullable = false,updatable = false, length = 85)
    private String sku;

    @Setter
    @Column(name = "name",nullable = false, length = 150)
    private String name;

    @Setter
    @Column(name = "description", nullable = true, length = 300)
    private String description;

    @Setter
    @Column(name = "price",nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @OneToOne(mappedBy = "product",fetch = FetchType.LAZY)
    @Setter
    private Stock stock;

    public Product(AppUser createdBy, String sku, String name, String description, BigDecimal price) {
        super(createdBy);
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
