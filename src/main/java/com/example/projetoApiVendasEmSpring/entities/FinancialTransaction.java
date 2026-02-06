package com.example.projetoApiVendasEmSpring.entities;

import com.example.projetoApiVendasEmSpring.entities.enums.FinancialPaymentMethod;
import com.example.projetoApiVendasEmSpring.entities.enums.FinancialPaymentTerm;
import com.example.projetoApiVendasEmSpring.entities.enums.FinancialTransactionStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "financial_transaction")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FinancialTransaction extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_order_id",nullable = false)
    private SalesOrder salesOrder;
    @Setter
    @Column(name = "status",nullable = false,length = 30)
    @Enumerated(EnumType.STRING)
    private FinancialTransactionStatus status=FinancialTransactionStatus.OPEN;
    @Setter
    @Column(name = "payment_method",nullable = false,length = 30)
    @Enumerated(EnumType.STRING)
    private FinancialPaymentMethod paymentMethod;
    @Setter
    @Column(name = "payment_term",nullable = false,length = 30)
    @Enumerated(EnumType.STRING)
    private FinancialPaymentTerm paymentTerm;
    @Setter
    @OneToMany(mappedBy = "financialTransaction",fetch = FetchType.LAZY)
    private List<Installment> installment;

    public FinancialTransaction(AppUser createdBy, SalesOrder salesOrder, FinancialPaymentMethod paymentMethod, FinancialPaymentTerm paymentTerm) {
        super(createdBy);
        this.salesOrder = salesOrder;
        this.paymentMethod = paymentMethod;
        this.paymentTerm = paymentTerm;
    }
}
