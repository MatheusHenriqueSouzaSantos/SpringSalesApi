package com.example.projetoApiVendasEmSpring.salesOrder.util;

import com.example.projetoApiVendasEmSpring.financialTransaction.dto.InstallmentOutputDto;
import com.example.projetoApiVendasEmSpring.salesOrder.entitiy.SalesOrderItem;

import java.math.BigDecimal;
import java.util.List;

public final class FinancialSalesOrderUtil {
    public static BigDecimal sumSubTotalAmountBySalesOrder(List<SalesOrderItem> items){
        BigDecimal salesOrderSubTotalAmount=new BigDecimal("0");
        for(SalesOrderItem item: items){
            BigDecimal quantity=BigDecimal.valueOf(item.getQuantity());
            BigDecimal itemTotal=(item.getUnitPrice().multiply(quantity).subtract(item.getDiscountAmount()));
            salesOrderSubTotalAmount=salesOrderSubTotalAmount.add(itemTotal);
        }
        return salesOrderSubTotalAmount;
    }

    public static long countInstallments(List<InstallmentOutputDto> installmentsDto){
        return installmentsDto.stream().filter(InstallmentOutputDto::active).count();
    }

    public static long countPaidInstallments(List<InstallmentOutputDto> installmentsDto) {
        return installmentsDto.stream().filter(InstallmentOutputDto::active).filter(InstallmentOutputDto::paid).count();
    }

    public static BigDecimal sumOfPaidInstallments(List<InstallmentOutputDto> installmentsDto){
        return installmentsDto.stream().filter(InstallmentOutputDto::active).filter(InstallmentOutputDto::paid).map(InstallmentOutputDto::installmentAmount)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }
}
