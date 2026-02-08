package com.example.projetoApiVendasEmSpring.services;

import com.example.projetoApiVendasEmSpring.dtos.appUser.AuditAppUserDto;
import com.example.projetoApiVendasEmSpring.dtos.financialTransaction.FinancialTransactionOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.installment.InstallmentOutputDto;
import com.example.projetoApiVendasEmSpring.entities.AppUser;
import com.example.projetoApiVendasEmSpring.entities.FinancialTransaction;
import com.example.projetoApiVendasEmSpring.entities.Installment;
import com.example.projetoApiVendasEmSpring.entities.SalesOrder;
import com.example.projetoApiVendasEmSpring.entities.enums.FinancialTransactionStatus;
import com.example.projetoApiVendasEmSpring.entities.enums.SalesOrderStatus;
import com.example.projetoApiVendasEmSpring.excepetions.BusinessException;
import com.example.projetoApiVendasEmSpring.excepetions.ResourceNotFoundException;
import com.example.projetoApiVendasEmSpring.repositories.AppUserRepository;
import com.example.projetoApiVendasEmSpring.repositories.FinancialTransactionRepository;
import com.example.projetoApiVendasEmSpring.repositories.InstallmentRepository;
import com.example.projetoApiVendasEmSpring.repositories.SalesOrderRepository;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;
import com.example.projetoApiVendasEmSpring.services.interfaces.FinancialTransactionService;
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
        FinancialTransaction financialTransaction=repository.findByIdAndActiveTrue(financialTransactionId)
                .orElseThrow(()->new ResourceNotFoundException("Financial transaction not found"));
        List<Installment> installments=financialTransaction.getInstallments();
        SalesOrder salesOrder=salesOrderRepository.findByActiveTrueAndFinancialTransactionId(financialTransaction.getId())
                .orElseThrow(()->new ResourceNotFoundException("Sales order not found"));
        AppUser updatedBy=appUserRepository.findActiveAppUserByIdExceptSystemUser(SystemUser.ID,loggedUser.getId())
                .orElseThrow(()->new ResourceNotFoundException("User not found"));
        int installmentCount=installmentRepository.countByFinancialTransactionIdAndActiveTrue(financialTransactionId);
        int paidInstallmentCount=installmentRepository.countByFinancialTransactionIdAndActiveTrueAndPaidTrue(financialTransactionId);
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
        installmentCount=installmentRepository.countByFinancialTransactionIdAndActiveTrue(financialTransactionId);
        paidInstallmentCount=installmentRepository.countByFinancialTransactionIdAndActiveTrueAndPaidTrue(financialTransactionId);
        if(installmentCount==paidInstallmentCount){
            financialTransaction.setStatus(FinancialTransactionStatus.PAID);
        }
        return installments.stream().map(this::installmentEntityToDto).toList();
    }

    private FinancialTransactionOutputDto financialTransactionEntityToDto(FinancialTransaction financialTransaction){
        AuditAppUserDto createdBy= AuditAppUserDto.appUserToAuditAppUserDto(financialTransaction.getCreatedBy());
        AuditAppUserDto updatedBy=AuditAppUserDto.appUserToAuditAppUserDto(financialTransaction.getUpdatedBy());
        List<InstallmentOutputDto> installmentsDto=financialTransaction.getInstallments().stream().map(this::installmentEntityToDto).toList();
        int installmentCount=installmentRepository.countByFinancialTransactionIdAndActiveTrue(financialTransaction.getId());
        int paidInstallmentCount=installmentRepository.countByFinancialTransactionIdAndActiveTrueAndPaidTrue(financialTransaction.getId());
        BigDecimal paidTotalAmount=installmentRepository.getSumOfPaidInstallmentsByFinancialTransactionId(financialTransaction.getId());
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
