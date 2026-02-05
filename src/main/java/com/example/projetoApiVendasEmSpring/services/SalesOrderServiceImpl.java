package com.example.projetoApiVendasEmSpring.services;

import com.example.projetoApiVendasEmSpring.dtos.appUser.AuditAppUserDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.simplifyCustomerOutputDto.SimplifyCorporateCustomerOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.simplifyCustomerOutputDto.SimplifyCustomerOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.simplifyCustomerOutputDto.SimplifyIndividualCustomerOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.financialTransaction.FinancialTransactionOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.installment.InstallmentOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.product.SimplifyProductOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.salesOrder.SalesOrderInputDto;
import com.example.projetoApiVendasEmSpring.dtos.salesOrder.SalesOrderOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.salesOrderItem.SalesOrderItemOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.seller.SimplifySellerOutputDto;
import com.example.projetoApiVendasEmSpring.entities.*;
import com.example.projetoApiVendasEmSpring.excepetions.BusinessException;
import com.example.projetoApiVendasEmSpring.excepetions.ResourceNotFoundException;
import com.example.projetoApiVendasEmSpring.repositories.InstallmentRepository;
import com.example.projetoApiVendasEmSpring.repositories.SalesOrderRepository;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;
import com.example.projetoApiVendasEmSpring.services.interfaces.SalesOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final InstallmentRepository installmentRepository;

    public SalesOrderServiceImpl(SalesOrderRepository salesOrderRepository, InstallmentRepository installmentRepository) {
        this.salesOrderRepository = salesOrderRepository;
        this.installmentRepository = installmentRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<SalesOrderOutputDto> getAll() {
        return salesOrderRepository.findSalesOrderOrderingByStatus().stream()
                .map(this::entityToDto).toList();
    }
    @Override
    public SalesOrderOutputDto getById(UUID id) {
        SalesOrder salesOrder=salesOrderRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Sales Order not found"));
        return entityToDto(salesOrder);
    }

    @Override
    public SalesOrderOutputDto getByOrderCode(String orderCode) {
        if(orderCode.length()!=6){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Order code in invalid format");
        }
        SalesOrder salesOrder=salesOrderRepository.findByOrderCode(orderCode)
                .orElseThrow(()->new ResourceNotFoundException("Sales Order not found"));
        return entityToDto(salesOrder);
    }

    @Override
    public SalesOrderOutputDto create(SalesOrderInputDto dto, UserDetailsImpl loggedUser) {
        return null;
    }

    @Override
    public SalesOrderOutputDto update(UUID id, SalesOrderInputDto dto, UserDetailsImpl loggedUser) {
        return null;
    }

    private SalesOrderOutputDto entityToDto(SalesOrder salesOrder){
        List<SalesOrderItemOutputDto> itemsDto=salesOrderItemsEntityToDto(salesOrder.getSalesOrderItems());
        List<InstallmentOutputDto> installmentsDto=installmentEntityToDto(salesOrder.getFinancialTransaction().getInstallment());
        FinancialTransactionOutputDto financialTransactionDto=financialTransactionEntityToDto(salesOrder.getFinancialTransaction(),installmentsDto);
        return salesOrderEntityToDto(salesOrder,financialTransactionDto,itemsDto);
    }
    private List<SalesOrderItemOutputDto> salesOrderItemsEntityToDto(List<SalesOrderItem> items){
        List<SalesOrderItemOutputDto> itemsDto=new ArrayList<>();
        for(SalesOrderItem item : items){
            AuditAppUserDto createdBy= AuditAppUserDto.appUserToAuditAppUserDto(item.getCreatedBy());
            AuditAppUserDto updatedBy= AuditAppUserDto.appUserToAuditAppUserDto(item.getUpdatedBy());
            SimplifyProductOutputDto productDto=new SimplifyProductOutputDto(item.getProduct().getId(),item.getProduct().getName(),item.getProduct().getSku());
            SalesOrderItemOutputDto itemDto=new SalesOrderItemOutputDto(item.getId(),item.getCreatedAt(),createdBy, item.getUpdatedAt(),
            updatedBy,item.isActive(),productDto,item.getQuantity(),item.getUnitPrice(),item.getDiscountAmount());
            itemsDto.add(itemDto);
        }
        return itemsDto;
    }
    private List<InstallmentOutputDto> installmentEntityToDto(List<Installment> installments ){
        List<InstallmentOutputDto> installmentsDto=new ArrayList<>();
        for(Installment installment : installments){
            AuditAppUserDto createdBy=AuditAppUserDto.appUserToAuditAppUserDto(installment.getCreatedBy());
            AuditAppUserDto updatedBy=AuditAppUserDto.appUserToAuditAppUserDto(installment.getUpdatedBy());
            InstallmentOutputDto installmentDto= new InstallmentOutputDto(installment.getId(),installment.getCreatedAt(),createdBy,
                    installment.getUpdatedAt(),updatedBy,installment.isActive(),installment.getInstallmentAmount(),installment.getDueDate(), installment.isPaid());
            installmentsDto.add(installmentDto);
        }
        return installmentsDto;
    }
    private FinancialTransactionOutputDto financialTransactionEntityToDto(FinancialTransaction financialTransaction,List<InstallmentOutputDto> installmentsDto ){
        AuditAppUserDto createdBy=AuditAppUserDto.appUserToAuditAppUserDto(financialTransaction.getCreatedBy());
        AuditAppUserDto updatedBy=AuditAppUserDto.appUserToAuditAppUserDto(financialTransaction.getUpdatedBy());
        int installmentCount=installmentRepository.countByFinancialTransactionIdAndActiveTrue(financialTransaction.getId());
        int paidInstallmentCount=installmentRepository.countByFinancialTransactionIdAndActiveTrueAndPaidTrue(financialTransaction.getId());
        BigDecimal sumOfPaidInstallment=installmentRepository.getSumOfPaidInstallmentsByFinancialTransactionId(financialTransaction.getId());
        return new FinancialTransactionOutputDto(financialTransaction.getId(),financialTransaction.getCreatedAt(),
                createdBy,financialTransaction.getUpdatedAt(),updatedBy,financialTransaction.isActive(),financialTransaction.getStatus(),financialTransaction.getPaymentMethod(),
                financialTransaction.getPaymentTerm(),installmentsDto,installmentCount,paidInstallmentCount,sumOfPaidInstallment);
    }
    private SalesOrderOutputDto salesOrderEntityToDto(SalesOrder salesOrder,FinancialTransactionOutputDto financialTransactionOutputDto,
                                                      List<SalesOrderItemOutputDto> itemsDto){
        AuditAppUserDto createdBy=AuditAppUserDto.appUserToAuditAppUserDto(salesOrder.getCreatedBy());
        AuditAppUserDto updatedBy=AuditAppUserDto.appUserToAuditAppUserDto(salesOrder.getUpdatedBy());
        SimplifySellerOutputDto simplifySeller=new SimplifySellerOutputDto(salesOrder.getSeller().getId(),salesOrder.getSeller().getFullName(),
                salesOrder.getSeller().getCpf());
        SimplifyCustomerOutputDto simplifyCustomer;
        if(salesOrder.getCustomer() instanceof IndividualCustomer){
            simplifyCustomer= SimplifyIndividualCustomerOutputDto.individualCustomerEntityToSimplifyDto((IndividualCustomer) salesOrder.getCustomer());
        } else if (salesOrder.getCustomer() instanceof CorporateCustomer) {
            simplifyCustomer= SimplifyCorporateCustomerOutputDto.corporateCustomerEntityToSimplifyDto((CorporateCustomer) salesOrder.getCustomer());
        }
        else {
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Type of customer is invalid");
        }
        return new SalesOrderOutputDto(salesOrder.getId(),salesOrder.getCreatedAt(),createdBy,salesOrder.getUpdatedAt(),updatedBy,salesOrder.isActive(),
                salesOrder.getOrderCode(),salesOrder.getStatus(),simplifyCustomer,simplifySeller,itemsDto,salesOrder.getSubtotalAmount(),salesOrder.getOrderDiscountAmount(),
                salesOrder.getTotalAmount(),financialTransactionOutputDto);
    }


}
