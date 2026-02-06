package com.example.projetoApiVendasEmSpring.services.salesOrder;

import com.example.projetoApiVendasEmSpring.entities.SalesOrderItem;

import java.math.BigDecimal;
import java.util.List;

public final class SalesOrderUtil {
    public static BigDecimal sumSubTotalAmountBySalesOrder(List<SalesOrderItem> items){
        BigDecimal salesOrderSubTotalAmount=new BigDecimal("0");
        for(SalesOrderItem item: items){
            BigDecimal quantity=BigDecimal.valueOf(item.getQuantity());
            BigDecimal itemTotal=(item.getUnitPrice().multiply(quantity).subtract(item.getDiscountAmount()));
            salesOrderSubTotalAmount=salesOrderSubTotalAmount.add(itemTotal);
        }
        return salesOrderSubTotalAmount;
    }
}
