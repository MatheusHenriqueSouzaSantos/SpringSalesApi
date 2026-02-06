package com.example.projetoApiVendasEmSpring.services.validation;


import com.example.projetoApiVendasEmSpring.dtos.financialTransaction.FinancialTransactionInputDto;
import com.example.projetoApiVendasEmSpring.dtos.salesOrderItem.SalesOrderIteInputDto;
import com.example.projetoApiVendasEmSpring.entities.FinancialTransaction;
import com.example.projetoApiVendasEmSpring.entities.Product;
import com.example.projetoApiVendasEmSpring.entities.enums.FinancialPaymentTerm;
import com.example.projetoApiVendasEmSpring.excepetions.BusinessException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class SalesOrderValidation {

    public void validateSalesOrderItemForCreateOrThrow(SalesOrderIteInputDto itemDto, Product product){
        if(itemDto.quantity()> product.getStock().getQuantity()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Quantity insufficient of the product with name: "+ product.getName());
        }
        BigDecimal quantity=BigDecimal.valueOf(itemDto.quantity());
        BigDecimal itemTotalAmount=product.getPrice().multiply(quantity);
        if(itemDto.discountAmount().compareTo(itemTotalAmount)>=0){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The discount Amount must no be greater than item total, productId of item: "
                    + product.getName());
        }
    }
    public void validateFinancialTransactionForCreateOrThrow(FinancialTransactionInputDto dto){
        if(dto.financialPaymentTerm()== FinancialPaymentTerm.FULL_PAYMENT && dto.installmentCount()!=1){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"If financial payment term is full payment the count of installment must be 1");
        }
        if(dto.financialPaymentTerm()==FinancialPaymentTerm.INSTALLMENT_PAYMENT && dto.installmentCount()==1){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"If financial payment term is installment payment the count of installment " +
                    "must be greater than 1");
        }
        if(dto.firstInstalmentDueDate().isBefore(LocalDate.now())){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The first installment due date must not be before today");
        }
    }
    public void validateInstallmentsForCreateOrThrow(FinancialTransactionInputDto dto){
        if(dto.installmentCount()>60){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"installment count must not be greater than 60");
        }
    }

}
