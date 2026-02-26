package com.example.projetoApiVendasEmSpring.financialTransaction.controller;

import com.example.projetoApiVendasEmSpring.financialTransaction.dto.FinancialTransactionOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.installment.InstallmentOutputDto;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;
import com.example.projetoApiVendasEmSpring.financialTransaction.service.FinancialTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/financiaslTransactions")
public class FinancialTransactionController {
    private FinancialTransactionService service;

    public FinancialTransactionController(FinancialTransactionService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinancialTransactionOutputDto> findById(@PathVariable UUID id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<List<InstallmentOutputDto>> payInstallment(@PathVariable UUID id, @AuthenticationPrincipal UserDetailsImpl loggedUser){
        return ResponseEntity.ok(service.payInstallment(id,loggedUser));
    }
}
