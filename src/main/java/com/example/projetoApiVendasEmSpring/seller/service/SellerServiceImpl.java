package com.example.projetoApiVendasEmSpring.seller.service;

import com.example.projetoApiVendasEmSpring.appUser.dto.AuditAppUserDto;
import com.example.projetoApiVendasEmSpring.seller.dto.SellerCreateDto;
import com.example.projetoApiVendasEmSpring.seller.dto.SellerOutputDto;
import com.example.projetoApiVendasEmSpring.seller.dto.SellerUpdateDto;
import com.example.projetoApiVendasEmSpring.appUser.entity.AppUser;
import com.example.projetoApiVendasEmSpring.seller.entity.Seller;
import com.example.projetoApiVendasEmSpring.excepetions.BusinessException;
import com.example.projetoApiVendasEmSpring.excepetions.ResourceNotFoundException;
import com.example.projetoApiVendasEmSpring.appUser.repository.AppUserRepository;
import com.example.projetoApiVendasEmSpring.seller.repository.SellerRepository;
import com.example.projetoApiVendasEmSpring.security.userDetails.UserDetailsImpl;
import com.example.projetoApiVendasEmSpring.SystemUser;
import com.example.projetoApiVendasEmSpring.customer.validation.DocumentValidation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Service
public class SellerServiceImpl implements SellerService {

    private final SellerRepository repository;

    private final AppUserRepository appUserRepository;

    private final DocumentValidation documentValidation;

    public SellerServiceImpl(SellerRepository repository, AppUserRepository appUserRepository, DocumentValidation documentValidation) {
        this.repository = repository;
        this.appUserRepository = appUserRepository;
        this.documentValidation = documentValidation;
    }

    @Transactional(readOnly = true)
    @Override
    public List<SellerOutputDto> findAll() {
        return repository.findAllByOrderByActiveDesc().stream().map(this::entityToDto).toList();
    }
    @Transactional(readOnly = true)
    @Override
    public SellerOutputDto findSellerById(UUID id) {
        Seller seller=getSellerAccordingMethodOrThrow(()->repository.findById(id));
        return entityToDto(seller);
    }
    @Transactional(readOnly = true)
    @Override
    public SellerOutputDto findActiveSellerById(UUID id) {
        Seller seller=getSellerAccordingMethodOrThrow(()->repository.findByIdAndActiveTrue(id));
        return entityToDto(seller);
    }
    @Transactional(readOnly = true)
    @Override
    public SellerOutputDto findSellerByCpf(String cpf) {
        if(!documentValidation.validateCpf(cpf)){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The cpf must be valid");
        }
        Seller seller=getSellerAccordingMethodOrThrow(()->repository.findByCpf(cpf));
        return entityToDto(seller);
    }
    @Transactional(readOnly = true)
    @Override
    public SellerOutputDto findActiveSellerByCpf(String cpf) {
        if(!documentValidation.validateCpf(cpf)){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The cpf must be valid");
        }
        Seller seller=getSellerAccordingMethodOrThrow(()->repository.findByCpfAndActiveTrue(cpf));
        return entityToDto(seller);
    }
    @Transactional
    @Override
    public SellerOutputDto createSeller(SellerCreateDto dto, UserDetailsImpl loggedUser) {
        if(!documentValidation.validateCpf(dto.cpf())){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The cpf must be valid");
        }
        if(repository.existsByCpf(dto.cpf())){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"already exists a seller with this cpf");
        }
        Optional<Seller> sellerExistsWithEmailReceived=repository.findByEmail(dto.email());

        if(sellerExistsWithEmailReceived.isPresent()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"already exists a seller with this email");
        }
        AppUser createdBy= getActiveAppUserByIdOrThrow(loggedUser.getId());
        Seller seller=new Seller(createdBy,dto.fullName(), dto.cpf(),dto.email(), dto.phone());
        repository.save(seller);
        return entityToDto(seller);
    }
    @Transactional
    @Override
    public SellerOutputDto updateSeller(UUID id, SellerUpdateDto dto, UserDetailsImpl loggedUser) {
        Seller updatedSeller=getSellerAccordingMethodOrThrow(()->repository.findById(id));
        if(!updatedSeller.isActive()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"the Seller must be active to update");
        }
        Optional<Seller> sellerExistsWithEmailReceived=repository.findByEmail(dto.email());
        if(sellerExistsWithEmailReceived.isPresent() && sellerExistsWithEmailReceived.get().getId()!=id){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"already exists a seller with this email");
        }
        AppUser updatedBy= getActiveAppUserByIdOrThrow(loggedUser.getId());
        updatedSeller.setUpdatedBy(updatedBy);
        updatedSeller.setFullName(dto.fullName());
        updatedSeller.setEmail(dto.email());
        updatedSeller.setPhone(dto.phone());
        return entityToDto(updatedSeller);
    }

    @Transactional
    @Override
    public void deActivateSeller(UUID id, UserDetailsImpl loggedUser) {
        Seller seller=getSellerAccordingMethodOrThrow(()->repository.findById(id));
        if(!seller.isActive()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The seller is already inactive");
        }
        AppUser updatedBy= getActiveAppUserByIdOrThrow(loggedUser.getId());
        seller.setUpdatedAt(Instant.now());
        seller.setUpdatedBy(updatedBy);
        seller.setActive(false);
    }
    @Transactional
    @Override
    public void reActivateSeller(UUID id, UserDetailsImpl loggedUser) {
        Seller seller=getSellerAccordingMethodOrThrow(()->repository.findById(id));
        if(seller.isActive()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The seller is already active");
        }
        AppUser updatedBy= getActiveAppUserByIdOrThrow(loggedUser.getId());
        seller.setUpdatedAt(Instant.now());
        seller.setUpdatedBy(updatedBy);
        seller.setActive(true);
    }

    private SellerOutputDto entityToDto(Seller seller){
        AuditAppUserDto createdBy= AuditAppUserDto.appUserToAuditAppUserDto(seller.getCreatedBy());
        AuditAppUserDto updatedBy= AuditAppUserDto.appUserToAuditAppUserDto(seller.getUpdatedBy());

        return new SellerOutputDto(seller.getId(),seller.getCreatedAt(),createdBy,seller.getUpdatedAt(),updatedBy,seller.isActive(),
                seller.getFullName(),seller.getCpf(),seller.getEmail(), seller.getPhone());
    }

    private Seller getSellerAccordingMethodOrThrow(Supplier<Optional<Seller>> supplier){
        return supplier.get().orElseThrow(()->new ResourceNotFoundException("Seller not found"));
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
