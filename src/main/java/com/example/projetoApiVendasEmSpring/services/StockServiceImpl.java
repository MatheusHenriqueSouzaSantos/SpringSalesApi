package com.example.projetoApiVendasEmSpring.services;

import com.example.projetoApiVendasEmSpring.dtos.stock.StockInputDto;
import com.example.projetoApiVendasEmSpring.dtos.stock.StockOutputDto;
import com.example.projetoApiVendasEmSpring.entities.AppUser;
import com.example.projetoApiVendasEmSpring.entities.Stock;
import com.example.projetoApiVendasEmSpring.excepetions.BusinessException;
import com.example.projetoApiVendasEmSpring.excepetions.ResourceNotFoundException;
import com.example.projetoApiVendasEmSpring.repositories.AppUserRepository;
import com.example.projetoApiVendasEmSpring.repositories.StockRepository;
import com.example.projetoApiVendasEmSpring.security.SecurityUtils;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;
import com.example.projetoApiVendasEmSpring.services.interfaces.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class StockServiceImpl implements StockService {

    private final StockRepository repository;

    private final AppUserRepository appUserRepository;

    private final SecurityUtils util;

    public StockServiceImpl(StockRepository repository, AppUserRepository appUserRepository, SecurityUtils util) {
        this.repository = repository;
        this.appUserRepository = appUserRepository;
        this.util = util;
    }

    @Transactional(readOnly = true)
    @Override
    public StockOutputDto findById(UUID id) {
        Stock stock=repository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Stock not found"));
        return entityToDto(stock);
    }

    @Override
    public StockOutputDto findByProductId(UUID productId) {
        Stock stock=repository.findByProductId(productId)
                .orElseThrow(()->new ResourceNotFoundException("Stock not found"));

        return entityToDto(stock);
    }

    @Override
    @Transactional
    public StockOutputDto increaseQuantity(UUID id, StockInputDto dto, UserDetailsImpl loggedUser) {
        Stock stock= getStockByIdAndActiveOrThrow(id);
        AppUser updatedBy= getActiveAppUserByIdOrThrow(loggedUser.getId());
        stock.setUpdatedBy(updatedBy);
        stock.increaseQuantity(dto.quantity());

        return entityToDto(stock);
    }

    @Override
    @Transactional
    public StockOutputDto decreaseQuantity(UUID id, StockInputDto dto, UserDetailsImpl loggedUser) {
        Stock stock= getStockByIdAndActiveOrThrow(id);

        if(dto.quantity()>stock.getQuantity()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Insufficient quantity in stock");
        }
        AppUser updatedBy= getActiveAppUserByIdOrThrow(loggedUser.getId());
        stock.setUpdatedBy(updatedBy);

        stock.decreaseQuantity(dto.quantity());
        return entityToDto(stock);
    }

    private StockOutputDto entityToDto(Stock stock){
        return new StockOutputDto(stock.getId(),stock.getProduct().getId(),stock.getQuantity());
    }

    private Stock getStockByIdAndActiveOrThrow(UUID id){
        return repository.findByIdAndActiveTrue(id).
                orElseThrow(()->new ResourceNotFoundException("Stock not found"));
    }
    private AppUser getActiveAppUserByIdOrThrow(UUID id){
        AppUser appUser= appUserRepository.findAppUserByIdExceptSystemUser(SystemUser.ID,id).
                orElseThrow(()->new ResourceNotFoundException("User not found"));
        if(!appUser.isActive()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"App user is inactive and can not do any action");
        }
        return appUser;
    }
}
