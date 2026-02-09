package com.example.projetoApiVendasEmSpring.services;

import com.example.projetoApiVendasEmSpring.dtos.appUser.AuditAppUserDto;
import com.example.projetoApiVendasEmSpring.dtos.product.ProductCreateDto;
import com.example.projetoApiVendasEmSpring.dtos.product.ProductOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.product.ProductUpdateDto;
import com.example.projetoApiVendasEmSpring.dtos.product.SummaryStockDto;
import com.example.projetoApiVendasEmSpring.entities.AppUser;
import com.example.projetoApiVendasEmSpring.entities.Product;
import com.example.projetoApiVendasEmSpring.entities.Stock;
import com.example.projetoApiVendasEmSpring.excepetions.BusinessException;
import com.example.projetoApiVendasEmSpring.excepetions.ResourceNotFoundException;
import com.example.projetoApiVendasEmSpring.repositories.AppUserRepository;
import com.example.projetoApiVendasEmSpring.repositories.ProductRepository;
import com.example.projetoApiVendasEmSpring.repositories.StockRepository;
import com.example.projetoApiVendasEmSpring.security.SecurityUtils;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;
import com.example.projetoApiVendasEmSpring.services.interfaces.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    ProductRepository repository;

    AppUserRepository appUserRepository;

    StockRepository stockRepository;

    SecurityUtils util;

    private final int INIT_QUANTITY=0;

    public ProductServiceImpl(ProductRepository repository, AppUserRepository appUserRepository, StockRepository stockRepository, SecurityUtils util) {
        this.repository = repository;
        this.appUserRepository = appUserRepository;
        this.stockRepository = stockRepository;
        this.util = util;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductOutputDto> findAllProduct() {
        List<Product> products=repository.findAllByOrderByActiveDesc();
        return products.stream().map(this::entityToDto).toList();

    }


    @Transactional(readOnly = true)
    @Override
    public ProductOutputDto findProductById(UUID id) {
        return entityToDto(getProductByIdOrThrow(id));
    }

    @Override
    public ProductOutputDto findActiveProductById(UUID id) {
        Product product=repository.findByIdAndActiveTrue(id)
                .orElseThrow(()->new ResourceNotFoundException("Product not found"));
        return entityToDto(product);
    }


    @Transactional(readOnly = true)
    @Override
    public ProductOutputDto findProductBySku(String sku) {
        return entityToDto(repository.findProductBySku(sku)
                .orElseThrow(()->new ResourceNotFoundException("Product not found")));
    }

    @Override
    public ProductOutputDto findActiveProductBySku(String sku) {
        Product product=repository.findProductBySkuAndActiveTrue(sku)
                .orElseThrow(()->new ResourceNotFoundException("Product not found"));
        return entityToDto(product);
    }


    @Override
    @Transactional
    public ProductOutputDto createProduct(ProductCreateDto dto, UserDetailsImpl loggedUser) {
        if(repository.existsBySku(dto.sku())){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"a product whit this SKU already exists");
        }
        AppUser createdBy= getAppUserByIdOrThrow(loggedUser.getId());

        BigDecimal price= new BigDecimal(dto.price());

        validatePriceGreaterThanZero(price);

        Product product=new Product(createdBy,dto.sku(),dto.name(),dto.description(),price);
        Stock stock=new Stock(createdBy,product,INIT_QUANTITY);
        product.setStock(stock);

        stockRepository.save(stock);
        return entityToDto(repository.save(product));
    }

    @Override
    @Transactional
    public ProductOutputDto updateProduct(UUID id,ProductUpdateDto dto, UserDetailsImpl loggedUser) {
        Product product= getProductByIdOrThrow(id);

        if(!product.isActive()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The product is inactive, active it to update");
        }

        BigDecimal price=new BigDecimal(dto.price());

        validatePriceGreaterThanZero(price);

        AppUser updatedBy= getAppUserByIdOrThrow(loggedUser.getId());

        product.setUpdatedAt(Instant.now());
        product.setUpdatedBy(updatedBy);
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(price);

        return entityToDto(product);
    }

    @Override
    @Transactional
    public void deActivateProductById(UUID id, UserDetailsImpl loggedUser) {
        Product product= getProductByIdOrThrow(id);

        if(!product.isActive()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The product is already inactivated");
        }

        AppUser updatedBy= getAppUserByIdOrThrow(loggedUser.getId());

        product.setUpdatedAt(Instant.now());
        product.setUpdatedBy(updatedBy);
        product.setActive(false);

        product.getStock().setActive(false);
    }

    @Override
    public void reActivateProductById(UUID id, UserDetailsImpl loggedUser) {
        Product product= getProductByIdOrThrow(id);

        if(product.isActive()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The product already active");
        }

        AppUser updatedBy= getAppUserByIdOrThrow(loggedUser.getId());

        product.setUpdatedAt(Instant.now());
        product.setUpdatedBy(updatedBy);
        product.setActive(true);

        product.getStock().setActive(true);
    }


    private ProductOutputDto entityToDto(Product product){
        AuditAppUserDto createdBy= AuditAppUserDto.appUserToAuditAppUserDto(product.getCreatedBy());
        AuditAppUserDto updatedBy= AuditAppUserDto.appUserToAuditAppUserDto(product.getUpdatedBy());
        Stock stock=product.getStock();
        SummaryStockDto stockDto=new SummaryStockDto(stock.getId(), stock.getQuantity());
        return new ProductOutputDto(
                product.getId(),
                product.getCreatedAt(),
                createdBy,
                product.getUpdatedAt(),
                updatedBy,
                product.isActive(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                stockDto);
    }

    private void validatePriceGreaterThanZero(BigDecimal price){
        if(price.compareTo(BigDecimal.ZERO) <=0){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The price must be greater than zero");
        }
    }

    private AppUser getAppUserByIdOrThrow(UUID id){
        AppUser appUser= appUserRepository.findAppUserByIdExceptSystemUser(SystemUser.ID,id).
                orElseThrow(()->new ResourceNotFoundException("User not found"));
        if(!appUser.isActive()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"App user is inactive and can not do any action");
        }
        return appUser;
    }

    private Product getProductByIdOrThrow(UUID id){
        return repository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Product not found"));
    }
}
