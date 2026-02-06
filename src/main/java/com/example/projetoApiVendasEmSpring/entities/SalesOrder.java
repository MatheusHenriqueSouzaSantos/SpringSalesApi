package com.example.projetoApiVendasEmSpring.entities;

import com.example.projetoApiVendasEmSpring.entities.enums.SalesOrderStatus;
import com.example.projetoApiVendasEmSpring.services.Utils;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "sales_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SalesOrder extends BaseEntity {
    @Column(name = "order_code",nullable = false,updatable = false,unique = true,length = 6)
    private String orderCode;
    //can alter ids of fks?
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",nullable = false)
    private Customer customer;
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;
    @Setter
    @Column(name = "subtotal_amount",nullable = false,precision = 15,scale = 2)
    private BigDecimal subtotalAmount;
    @Setter
    @Column(name = "order_discount_amount",nullable = false,precision = 15,scale = 2)
    private BigDecimal orderDiscountAmount;
    @Setter
    @Column(name = "total_amount",nullable = false,precision = 15,scale = 2)
    private BigDecimal totalAmount;
    @Setter
    @OneToMany(mappedBy = "salesOrder")
    private List<SalesOrderItem> salesOrderItems;
    @Setter
    @OneToOne(mappedBy = "salesOrder")
    private FinancialTransaction financialTransaction;

    @Setter
    @Column(name = "status",nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SalesOrderStatus status=SalesOrderStatus.CREATED;


    public SalesOrder(AppUser createdBy, Customer customer, Seller seller) {
        super(createdBy);
        this.orderCode=Utils.GenerateOrderCode();
        this.customer = customer;
        this.seller = seller;
    }

    @Override
    protected void onPrePersist(){
        super.onPrePersist();
    }
}
