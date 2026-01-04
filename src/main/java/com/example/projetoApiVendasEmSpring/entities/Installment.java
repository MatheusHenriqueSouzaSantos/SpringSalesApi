package com.example.projetoApiVendasEmSpring.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "installment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Installment extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "financial_transaction_id",updatable = false,nullable = false)
    private FinancialTransaction financialTransaction;
    @Setter
    @Column(name = "installment_number",nullable = false)
    private int installmentNumber;
    @Setter
    @Column(name = "installment_amount",nullable = false)
    private BigDecimal installmentAmount;
    @Setter
    @Column(name = "due_date",nullable = false)
    private LocalDate dueDate;

    @Column(name = "paid",nullable = false)
    private boolean paid=false;

    public Installment(AppUser createdBy, FinancialTransaction financialTransaction, int installmentNumber, BigDecimal installmentAmount, LocalDate dueDate, boolean paid) {
        super(createdBy);
        this.financialTransaction = financialTransaction;
        this.installmentNumber = installmentNumber;
        this.installmentAmount = installmentAmount;
        this.dueDate = dueDate;
        this.paid = paid;
    }
}
