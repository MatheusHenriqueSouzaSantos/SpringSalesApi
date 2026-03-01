package com.example.projetoApiVendasEmSpring.salesOrder.validation;


import com.example.projetoApiVendasEmSpring.financialTransaction.dto.FinancialTransactionInputDto;
import com.example.projetoApiVendasEmSpring.salesOrder.dto.SalesOrderItemInputDto;
import com.example.projetoApiVendasEmSpring.financialTransaction.entity.FinancialTransaction;
import com.example.projetoApiVendasEmSpring.product.entity.Product;
import com.example.projetoApiVendasEmSpring.financialTransaction.entity.FinancialPaymentTerm;
import com.example.projetoApiVendasEmSpring.financialTransaction.entity.FinancialTransactionStatus;
import com.example.projetoApiVendasEmSpring.excepetions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public final class SalesOrderValidation {

    public void validateSalesOrderItemForCreateOrThrow(SalesOrderItemInputDto itemDto, Product product){
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
    }
    public void validateInstallmentsForCreateOrThrow(FinancialTransactionInputDto dto){
        if(dto.installmentCount()>60){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"installment count must not be greater than 60");
        }
    }

    public void validateIfASalesOrderCanBeModify(FinancialTransaction financialTransaction){
        if(financialTransaction.getStatus()== FinancialTransactionStatus.IN_PROGRESS){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The payment of salesOrder are starting, and sales order can not be modified");
        }
        if(financialTransaction.getStatus()==FinancialTransactionStatus.PAID){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The payment of salesOrder is completed, and sales order can not be modified");
        }
        if(financialTransaction.getStatus()==FinancialTransactionStatus.CANCELED){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The payment of salesOrder is already canceled, and sales order can not be modified");
        }
    }

}
