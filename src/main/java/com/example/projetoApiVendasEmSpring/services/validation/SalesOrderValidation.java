package com.example.projetoApiVendasEmSpring.services.validation;


import com.example.projetoApiVendasEmSpring.dtos.financialTransaction.FinancialTransactionInputDto;
import com.example.projetoApiVendasEmSpring.dtos.salesOrderItem.SalesOrderItemInputDto;
import com.example.projetoApiVendasEmSpring.entities.FinancialTransaction;
import com.example.projetoApiVendasEmSpring.entities.Product;
import com.example.projetoApiVendasEmSpring.entities.enums.FinancialPaymentTerm;
import com.example.projetoApiVendasEmSpring.entities.enums.FinancialTransactionStatus;
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
