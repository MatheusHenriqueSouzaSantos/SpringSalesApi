package com.example.projetoApiVendasEmSpring.stock.entity;


import com.example.projetoApiVendasEmSpring.appUser.entity.AppUser;
import com.example.projetoApiVendasEmSpring.entities.BaseEntity;
import com.example.projetoApiVendasEmSpring.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stock")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, updatable = false)
    private Product product;
    @Setter
    @Column(name = "quantity",nullable = false)
    private int quantity;

    public Stock(AppUser createdBy, Product product, int quantity) {
        super(createdBy);
        this.product = product;
        this.quantity = quantity;
    }
    public void increaseQuantity(int quantity){
        this.quantity+=quantity;
    }
    public void decreaseQuantity(int quantity){
        this.quantity-=quantity;
    }
}
