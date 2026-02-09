package com.example.projetoApiVendasEmSpring.services.salesOrder;

import com.example.projetoApiVendasEmSpring.dtos.appUser.AuditAppUserDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.simplifyCustomerOutputDto.SimplifyCorporateCustomerOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.simplifyCustomerOutputDto.SimplifyCustomerOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.simplifyCustomerOutputDto.SimplifyIndividualCustomerOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.financialTransaction.FinancialTransactionInputDto;
import com.example.projetoApiVendasEmSpring.dtos.financialTransaction.FinancialTransactionOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.installment.InstallmentOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.product.SimplifyProductOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.salesOrder.SalesOrderInputDto;
import com.example.projetoApiVendasEmSpring.dtos.salesOrder.SalesOrderOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.salesOrderItem.SalesOrderItemInputDto;
import com.example.projetoApiVendasEmSpring.dtos.salesOrderItem.SalesOrderItemOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.seller.SimplifySellerOutputDto;
import com.example.projetoApiVendasEmSpring.entities.*;
import com.example.projetoApiVendasEmSpring.entities.enums.FinancialTransactionStatus;
import com.example.projetoApiVendasEmSpring.entities.enums.SalesOrderStatus;
import com.example.projetoApiVendasEmSpring.excepetions.BusinessException;
import com.example.projetoApiVendasEmSpring.excepetions.ResourceNotFoundException;
import com.example.projetoApiVendasEmSpring.repositories.*;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;
import com.example.projetoApiVendasEmSpring.services.SystemUser;
import com.example.projetoApiVendasEmSpring.services.interfaces.SalesOrderService;
import com.example.projetoApiVendasEmSpring.services.validation.SalesOrderValidation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final InstallmentRepository installmentRepository;
    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final AppUserRepository appUserRepository;
    private final ProductRepository productRepository;
    private final SalesOrderValidation salesOrderValidation;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final FinancialTransactionRepository financialTransactionRepository;

    public SalesOrderServiceImpl(SalesOrderRepository salesOrderRepository, InstallmentRepository installmentRepository,
                                 CustomerRepository customerRepository, SellerRepository sellerRepository, AppUserRepository appUserRepository,
                                 ProductRepository productRepository, SalesOrderValidation salesOrderValidation,
                                 SalesOrderItemRepository salesOrderItemRepository, FinancialTransactionRepository financialTransactionRepository) {
        this.salesOrderRepository = salesOrderRepository;
        this.installmentRepository = installmentRepository;
        this.customerRepository = customerRepository;
        this.sellerRepository = sellerRepository;
        this.appUserRepository = appUserRepository;
        this.productRepository = productRepository;
        this.salesOrderValidation = salesOrderValidation;
        this.salesOrderItemRepository = salesOrderItemRepository;
        this.financialTransactionRepository = financialTransactionRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<SalesOrderOutputDto> findAll() {
        return salesOrderRepository.findSalesOrderOrderingByStatus().stream()
                .map(this::entityToDto).toList();
    }
    @Transactional(readOnly = true)
    @Override
    public SalesOrderOutputDto findById(UUID id) {
        SalesOrder salesOrder=salesOrderRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Sales Order not found"));
        return entityToDto(salesOrder);
    }

    @Transactional(readOnly = true)
    @Override
    public SalesOrderOutputDto findByOrderCode(String orderCode) {
        if(orderCode.length()!=6){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Order code in invalid format");
        }
        SalesOrder salesOrder=salesOrderRepository.findByOrderCode(orderCode)
                .orElseThrow(()->new ResourceNotFoundException("Sales Order not found"));
        return entityToDto(salesOrder);
    }
    @Transactional
    @Override
    public SalesOrderOutputDto create(SalesOrderInputDto dto, UserDetailsImpl loggedUser) {
        Customer customer=getCustomerByIdOrThrow(dto.customerId());
        Seller seller=getSellerByIdOrThrow(dto.sellerId());
        AppUser createdBy= getActiveAppUserByIdOrThrow(loggedUser.getId());
        SalesOrder salesOrder=new SalesOrder(createdBy,customer,seller);
        List<SalesOrderItem> salesOrderItems= createItems(dto.salesOrderItems(),salesOrder,createdBy);
        salesOrder.setSalesOrderItems(salesOrderItems);
        BigDecimal subTotalAmount=SalesOrderUtil.sumSubTotalAmountBySalesOrder(salesOrderItems);
        BigDecimal orderDiscountAmount=dto.orderDiscountAmount();
        if(orderDiscountAmount==null){
            orderDiscountAmount=BigDecimal.ZERO;
        }
        if(orderDiscountAmount.compareTo(subTotalAmount)>0){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The order discount amount must not be greater than items sum of sales order");
        }
        BigDecimal totalAmount=subTotalAmount.subtract(orderDiscountAmount);
        salesOrder.setSubtotalAmount(subTotalAmount);
        salesOrder.setOrderDiscountAmount(orderDiscountAmount);
        salesOrder.setTotalAmount(totalAmount);
        FinancialTransaction financialTransaction= createFinancialTransaction(dto.financialTransaction(),salesOrder,createdBy);
        salesOrder.setFinancialTransaction(financialTransaction);
        salesOrderRepository.save(salesOrder);
        return entityToDto(salesOrder);
    }
    @Transactional
    @Override
    public SalesOrderOutputDto update(UUID id, SalesOrderInputDto dto, UserDetailsImpl loggedUser) {
        SalesOrder salesOrder=salesOrderRepository.findByIdAndActiveTrue(id)
                .orElseThrow(()->new ResourceNotFoundException("Sales Order not found"));
        FinancialTransaction financialTransaction=salesOrder.getFinancialTransaction();
        salesOrderValidation.validateIfASalesOrderCanBeModify(financialTransaction);
        deleteSalesOrderItems(salesOrder.getSalesOrderItems());
        deleteInstallments(financialTransaction.getInstallments());
        Customer customer=getCustomerByIdOrThrow(dto.customerId());
        Seller seller=getSellerByIdOrThrow(dto.sellerId());
        AppUser updatedBy= getActiveAppUserByIdOrThrow(loggedUser.getId());
        salesOrder.setCustomer(customer);
        salesOrder.setSeller(seller);
        salesOrder.setUpdatedAt(Instant.now());
        salesOrder.setUpdatedBy(updatedBy);
        List<SalesOrderItem> newSalesOrderItems=createItems(dto.salesOrderItems(),salesOrder,updatedBy);
        salesOrder.setSalesOrderItems(newSalesOrderItems);
        BigDecimal subTotalAmount=SalesOrderUtil.sumSubTotalAmountBySalesOrder(newSalesOrderItems);
        BigDecimal orderDiscountAmount=dto.orderDiscountAmount();
        if(orderDiscountAmount==null){
            orderDiscountAmount=BigDecimal.ZERO;
        }
        if(orderDiscountAmount.compareTo(subTotalAmount)>0){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The order discount amount must not be greater than items sum of sales order");
        }
        BigDecimal totalAmount=subTotalAmount.subtract(orderDiscountAmount);
        salesOrder.setSubtotalAmount(subTotalAmount);
        salesOrder.setOrderDiscountAmount(orderDiscountAmount);
        salesOrder.setTotalAmount(totalAmount);
        financialTransaction.setUpdatedAt(Instant.now());
        financialTransaction.setUpdatedBy(updatedBy);
        salesOrderValidation.validateFinancialTransactionForCreateOrThrow(dto.financialTransaction());
        salesOrderValidation.validateInstallmentsForCreateOrThrow(dto.financialTransaction());
        financialTransaction.setPaymentMethod(dto.financialTransaction().financialPaymentMethod());
        financialTransaction.setPaymentTerm(dto.financialTransaction().financialPaymentTerm());
        List<Installment> installments=createInstallments(financialTransaction,dto.financialTransaction(),totalAmount,updatedBy);
        financialTransaction.setInstallments(installments);
        return entityToDto(salesOrder);
    }
    @Transactional
    @Override
    public void cancelSalesOrder(UUID id, UserDetailsImpl loggedUser) {
        SalesOrder salesOrder=salesOrderRepository.findByIdAndActiveTrue(id)
                .orElseThrow(()->new ResourceNotFoundException("Sales Order not found"));
        FinancialTransaction financialTransaction=salesOrder.getFinancialTransaction();
        salesOrderValidation.validateIfASalesOrderCanBeModify(financialTransaction);
        inactiveSalesOrderItems(salesOrder.getSalesOrderItems());
        inactiveInstallments(financialTransaction.getInstallments());
        AppUser updatedBy= getActiveAppUserByIdOrThrow(loggedUser.getId());
        financialTransaction.setUpdatedAt(Instant.now());
        financialTransaction.setUpdatedBy(updatedBy);
        financialTransaction.setStatus(FinancialTransactionStatus.CANCELED);
        financialTransaction.setActive(false);
        salesOrder.setUpdatedAt(Instant.now());
        salesOrder.setUpdatedBy(updatedBy);
        salesOrder.setStatus(SalesOrderStatus.CANCELED);
        salesOrder.setActive(false);
    }

    private SalesOrderOutputDto entityToDto(SalesOrder salesOrder){
        List<SalesOrderItemOutputDto> itemsDto=salesOrderItemsEntityToDto(salesOrder.getSalesOrderItems());
        List<InstallmentOutputDto> installmentsDto=installmentEntityToDto(salesOrder.getFinancialTransaction().getInstallments());
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
            boolean overdue=installment.getDueDate().isBefore(LocalDate.now()) && !installment.isPaid();
            InstallmentOutputDto installmentDto= new InstallmentOutputDto(installment.getId(),installment.getCreatedAt(),createdBy,
                    installment.getUpdatedAt(),updatedBy,installment.isActive(),installment.getInstallmentAmount(),installment.getDueDate(),overdue,installment.isPaid());
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

    private Customer getCustomerByIdOrThrow(UUID id){
        Customer customer= customerRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Customer not found"));
        if(!customer.isActive()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Customer is inactive and can not be in SalesOrder");
        }
        return customer;
    }

    private Seller getSellerByIdOrThrow(UUID id){
        Seller seller=sellerRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Customer not found"));
        if(!seller.isActive()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"seller is inactive and can not be in SalesOrder");
        }
        return seller;
    }

    private AppUser getActiveAppUserByIdOrThrow(UUID id){
        AppUser appUser= appUserRepository.findAppUserByIdExceptSystemUser(SystemUser.ID,id).
                orElseThrow(()->new ResourceNotFoundException("User not found"));
        if(!appUser.isActive()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"App user is inactive and can not do any action");
        }
        return appUser;
    }

    private List<SalesOrderItem> createItems(List<SalesOrderItemInputDto> itemsDto, SalesOrder salesOrder, AppUser loggedUser){
        List<SalesOrderItem> salesOrderItems=new ArrayList<>();
        for (SalesOrderItemInputDto itemDto : itemsDto){
            Product product=productRepository.findByIdAndActiveTrue(itemDto.productId())
                    .orElseThrow(()->new ResourceNotFoundException("Product with id: "+ itemDto.productId() + " not found"));
            salesOrderValidation.validateSalesOrderItemForCreateOrThrow(itemDto,product);
            BigDecimal itemDiscountAmount=itemDto.discountAmount();
            if(itemDiscountAmount == null){
                itemDiscountAmount=BigDecimal.ZERO;
            }
            SalesOrderItem item=new SalesOrderItem(loggedUser,salesOrder,product,itemDto.quantity(),product.getPrice(),itemDiscountAmount);
            salesOrderItems.add(item);
        }
        salesOrderItemRepository.saveAll(salesOrderItems);
        return salesOrderItems;
    }
    private FinancialTransaction createFinancialTransaction(FinancialTransactionInputDto dto, SalesOrder salesOrder, AppUser loggedUser){
        salesOrderValidation.validateFinancialTransactionForCreateOrThrow(dto);
        FinancialTransaction financialTransaction=new FinancialTransaction(loggedUser,salesOrder,dto.financialPaymentMethod(),dto.financialPaymentTerm());
        List<Installment> installments=createInstallments(financialTransaction,dto,salesOrder.getTotalAmount(),loggedUser);
        financialTransaction.setInstallments(installments);
        financialTransactionRepository.save(financialTransaction);
        return financialTransaction;
    }
    private List<Installment> createInstallments(FinancialTransaction financialTransaction, FinancialTransactionInputDto financialTransactionDto, BigDecimal orderTotalAmount, AppUser loggedUser){
        salesOrderValidation.validateInstallmentsForCreateOrThrow(financialTransactionDto);
        LocalDate firstInstallmentDueDate=financialTransactionDto.firstInstalmentDueDate();
        if(firstInstallmentDueDate==null){
            firstInstallmentDueDate=LocalDate.now().plusMonths(1);
        }
        List<Installment> installments=new ArrayList<>();
        BigDecimal bigDecimalInstallmentCount=BigDecimal.valueOf(financialTransactionDto.installmentCount());
        BigDecimal installmentBaseValue=orderTotalAmount.divide(bigDecimalInstallmentCount,2, RoundingMode.DOWN);
        for (int i=0;i<financialTransactionDto.installmentCount();i++){
            Installment installment=new Installment(loggedUser,financialTransaction,i+1,installmentBaseValue,firstInstallmentDueDate.plusMonths(i));
            installments.add(installment);
        }
        BigDecimal rest=(orderTotalAmount.subtract(installmentBaseValue.multiply(bigDecimalInstallmentCount)));
        Installment lastInstallment=installments.getLast();
        lastInstallment.setInstallmentAmount(lastInstallment.getInstallmentAmount().add(rest));
        installmentRepository.saveAll(installments);
        return installments;
    }
    private void deleteSalesOrderItems(List<SalesOrderItem> salesOrderItems){
        for (SalesOrderItem item : salesOrderItems){
            salesOrderItemRepository.delete(item);
        }
    }
    private void deleteInstallments(List<Installment> installments){
        for (Installment installment: installments){
            installmentRepository.delete(installment);
        }
    }

    private void inactiveSalesOrderItems(List<SalesOrderItem> salesOrderItems){
        for (SalesOrderItem item : salesOrderItems){
            item.setActive(false);
        }
    }
    private void inactiveInstallments(List<Installment> installments){
        for (Installment installment: installments){
            installment.setActive(false);
        }
    }

}
