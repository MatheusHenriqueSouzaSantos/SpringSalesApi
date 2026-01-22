package com.example.projetoApiVendasEmSpring.services;

import com.example.projetoApiVendasEmSpring.dtos.stock.StockInputDto;
import com.example.projetoApiVendasEmSpring.dtos.stock.StockOutputDto;
import com.example.projetoApiVendasEmSpring.entities.Stock;
import com.example.projetoApiVendasEmSpring.excepetions.BusinessException;
import com.example.projetoApiVendasEmSpring.excepetions.ResourceNotFoundException;
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

    private final SecurityUtils util;

    public StockServiceImpl(StockRepository repository, SecurityUtils util) {
        this.repository = repository;
        this.util = util;
    }

    @Transactional(readOnly = true)
    @Override
    public StockOutputDto findById(UUID id) {
        if(util.isAdmin()){
            Stock stock=repository.findById(id)
                    .orElseThrow(()->new ResourceNotFoundException("Stock not found"));
            return entityToDto(stock);
        }
        return entityToDto(getStockByIdAndActiveOrThrow(id));
    }

    @Override
    public StockOutputDto findByProductId(UUID productId) {
        if(util.isAdmin()){
            Stock stock=repository.findByProductId(productId)
                    .orElseThrow(()->new ResourceNotFoundException("Stock not found"));

            return entityToDto(stock);
        }
        return entityToDto(repository.findByProductIdAndActiveTrue(productId)
                .orElseThrow(()->new ResourceNotFoundException("stock not found")));
    }

    @Override
    @Transactional
    public StockOutputDto increaseQuantity(UUID id, StockInputDto dto, UserDetailsImpl loggedUser) {
        Stock stock= getStockByIdAndActiveOrThrow(id);

        stock.setQuantity(stock.getQuantity()+dto.quantity());

        return entityToDto(stock);
    }

    @Override
    @Transactional
    public StockOutputDto decreaseQuantity(UUID id, StockInputDto dto, UserDetailsImpl loggedUser) {
        Stock stock= getStockByIdAndActiveOrThrow(id);

        if(dto.quantity()>stock.getQuantity()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Insufficient quantity in stock");
        }

        stock.setQuantity(stock.getQuantity()- dto.quantity());
        return entityToDto(stock);
    }

    private StockOutputDto entityToDto(Stock stock){
        return new StockOutputDto(stock.getId(),stock.getProduct().getId(),stock.getQuantity());
    }

    private Stock getStockByIdAndActiveOrThrow(UUID id){
        return repository.findByIdAndActiveTrue(id).
                orElseThrow(()->new ResourceNotFoundException("Stock not found"));
    }
}
