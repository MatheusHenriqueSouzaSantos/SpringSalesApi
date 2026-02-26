package com.example.projetoApiVendasEmSpring.salesOrderItem.entity;

import com.example.projetoApiVendasEmSpring.appUser.entity.AppUser;
import com.example.projetoApiVendasEmSpring.entities.BaseEntity;
import com.example.projetoApiVendasEmSpring.product.entity.Product;
import com.example.projetoApiVendasEmSpring.salesOrder.entities.SalesOrder;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "sales_order_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesOrderItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_order_id", nullable = false, updatable = false)
    private SalesOrder salesOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

    @Setter
    @Column(name = "quantity",nullable = false)
    private int quantity;
    @Setter
    @Column(name = "unit_price",nullable = false,updatable = false,precision = 15,scale = 2)
    private BigDecimal unitPrice;
    @Setter
    @Column(name = "discount_amount",nullable = false,precision = 15,scale = 2)
    private BigDecimal discountAmount;


    public SalesOrderItem(AppUser createdBy, SalesOrder salesOrder, Product product, int quantity, BigDecimal unitPrice, BigDecimal discountAmount) {
        super(createdBy);
        this.salesOrder = salesOrder;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discountAmount = discountAmount;
    }


}
