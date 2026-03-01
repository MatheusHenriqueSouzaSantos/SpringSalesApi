package com.example.projetoApiVendasEmSpring.financialTransaction.service;

import com.example.projetoApiVendasEmSpring.appUser.dto.AuditAppUserDto;
import com.example.projetoApiVendasEmSpring.financialTransaction.dto.FinancialTransactionOutputDto;
import com.example.projetoApiVendasEmSpring.financialTransaction.dto.InstallmentOutputDto;
import com.example.projetoApiVendasEmSpring.appUser.entity.AppUser;
import com.example.projetoApiVendasEmSpring.financialTransaction.entity.FinancialTransaction;
import com.example.projetoApiVendasEmSpring.financialTransaction.entity.Installment;
import com.example.projetoApiVendasEmSpring.salesOrder.entitiy.SalesOrder;
import com.example.projetoApiVendasEmSpring.financialTransaction.entity.FinancialTransactionStatus;
import com.example.projetoApiVendasEmSpring.salesOrder.entitiy.SalesOrderStatus;
import com.example.projetoApiVendasEmSpring.excepetions.BusinessException;
import com.example.projetoApiVendasEmSpring.excepetions.ResourceNotFoundException;
import com.example.projetoApiVendasEmSpring.appUser.repository.AppUserRepository;
import com.example.projetoApiVendasEmSpring.financialTransaction.repository.FinancialTransactionRepository;
import com.example.projetoApiVendasEmSpring.financialTransaction.repository.InstallmentRepository;
import com.example.projetoApiVendasEmSpring.salesOrder.repository.SalesOrderRepository;
import com.example.projetoApiVendasEmSpring.security.userDetails.UserDetailsImpl;
import com.example.projetoApiVendasEmSpring.salesOrder.util.FinancialSalesOrderUtil;
import com.example.projetoApiVendasEmSpring.SystemUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class FinancialTransactionServiceImpl implements FinancialTransactionService {

    private final FinancialTransactionRepository repository;

    private final InstallmentRepository installmentRepository;

    private final AppUserRepository appUserRepository;

    private final SalesOrderRepository salesOrderRepository;

    public FinancialTransactionServiceImpl(FinancialTransactionRepository repository, InstallmentRepository installmentRepository,
                                           AppUserRepository appUserRepository, SalesOrderRepository salesOrderRepository) {
        this.repository = repository;
        this.installmentRepository = installmentRepository;
        this.appUserRepository = appUserRepository;
        this.salesOrderRepository = salesOrderRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public FinancialTransactionOutputDto findById(UUID id) {
        FinancialTransaction financialTransaction=repository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Financial transaction not found"));
        return financialTransactionEntityToDto(financialTransaction);
    }

    @Transactional
    @Override
    public List<InstallmentOutputDto> payInstallment(UUID financialTransactionId, UserDetailsImpl loggedUser) {
        FinancialTransaction financialTransaction=repository.findById(financialTransactionId)
                .orElseThrow(()->new ResourceNotFoundException("Financial transaction not found"));
        if(!financialTransaction.isActive()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Financial transaction is inactive");
        }
        List<Installment> installments=financialTransaction.getInstallments();
        SalesOrder salesOrder=salesOrderRepository.findByActiveTrueAndFinancialTransactionId(financialTransaction.getId())
                .orElseThrow(()->new ResourceNotFoundException("Sales order not found"));
        AppUser updatedBy=appUserRepository.findAppUserByIdExceptSystemUser(SystemUser.ID,loggedUser.getId())
                .orElseThrow(()->new ResourceNotFoundException("User not found"));
        long installmentCount=installments.stream().filter(Installment::isActive).count();
        long paidInstallmentCount=installments.stream().filter(Installment::isActive).filter(Installment::isPaid).count();
        if(installmentCount==paidInstallmentCount){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"all installments are paid");
        }
        for(Installment installment: installments){
            if(!installment.isPaid()){
                financialTransaction.setStatus(FinancialTransactionStatus.IN_PROGRESS);
                salesOrder.setStatus(SalesOrderStatus.CLOSED);
                installment.setPaid(true);
                installment.setUpdatedAt(Instant.now());
                installment.setUpdatedBy(updatedBy);
                break;
            }
        }
        installmentCount=installments.stream().filter(Installment::isActive).count();
        paidInstallmentCount=installments.stream().filter(Installment::isActive).filter(Installment::isPaid).count();
        if(installmentCount==paidInstallmentCount){
            financialTransaction.setStatus(FinancialTransactionStatus.PAID);
        }
        return installments.stream().map(this::installmentEntityToDto).toList();
    }

    private FinancialTransactionOutputDto financialTransactionEntityToDto(FinancialTransaction financialTransaction){
        AuditAppUserDto createdBy= AuditAppUserDto.appUserToAuditAppUserDto(financialTransaction.getCreatedBy());
        AuditAppUserDto updatedBy=AuditAppUserDto.appUserToAuditAppUserDto(financialTransaction.getUpdatedBy());
        List<InstallmentOutputDto> installmentsDto=financialTransaction.getInstallments().stream().map(this::installmentEntityToDto).toList();
        long installmentCount= FinancialSalesOrderUtil.countInstallments(installmentsDto);
        long paidInstallmentCount= FinancialSalesOrderUtil.countPaidInstallments(installmentsDto);
        BigDecimal paidTotalAmount= FinancialSalesOrderUtil.sumOfPaidInstallments(installmentsDto);
        return new FinancialTransactionOutputDto(financialTransaction.getId(),financialTransaction.getCreatedAt(),createdBy,
                financialTransaction.getUpdatedAt(),updatedBy,financialTransaction.isActive(),financialTransaction.getStatus(),
                financialTransaction.getPaymentMethod(),financialTransaction.getPaymentTerm(),installmentsDto,installmentCount,paidInstallmentCount,
                paidTotalAmount);
    }
    private InstallmentOutputDto installmentEntityToDto(Installment installment){
        AuditAppUserDto createdBy=AuditAppUserDto.appUserToAuditAppUserDto(installment.getCreatedBy());
        AuditAppUserDto updatedBy=AuditAppUserDto.appUserToAuditAppUserDto(installment.getUpdatedBy());
        boolean overdue=installment.getDueDate().isBefore(LocalDate.now()) && !installment.isPaid();
        return new InstallmentOutputDto(installment.getId(),installment.getCreatedAt(),createdBy,installment.getUpdatedAt(),updatedBy,
                installment.isActive(), installment.getInstallmentAmount(),installment.getDueDate(),overdue,installment.isPaid());
    }
}
