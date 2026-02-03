package com.example.projetoApiVendasEmSpring.services;

import com.example.projetoApiVendasEmSpring.dtos.appUser.AppUserAuditDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.individualCustomer.IndividualCustomerCreateDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.individualCustomer.IndividualCustomerUpdateDto;
import com.example.projetoApiVendasEmSpring.dtos.seller.SellerCreateDto;
import com.example.projetoApiVendasEmSpring.dtos.seller.SellerOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.seller.SellerUpdateDto;
import com.example.projetoApiVendasEmSpring.entities.AppUser;
import com.example.projetoApiVendasEmSpring.entities.Seller;
import com.example.projetoApiVendasEmSpring.excepetions.BusinessException;
import com.example.projetoApiVendasEmSpring.excepetions.ResourceNotFoundException;
import com.example.projetoApiVendasEmSpring.repositories.AppUserRepository;
import com.example.projetoApiVendasEmSpring.repositories.SellerRepository;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;
import com.example.projetoApiVendasEmSpring.services.interfaces.SellerService;
import com.example.projetoApiVendasEmSpring.services.validation.DocumentValidation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
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
        AppUser createdBy=appUserRepository.findAppUserByIdExceptSystemUser(SystemUser.ID,loggedUser.getId())
                .orElseThrow(()->new ResourceNotFoundException("User not found"));
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
        AppUser updatedBy=appUserRepository.findAppUserByIdExceptSystemUser(SystemUser.ID,loggedUser.getId())
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));
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
        AppUser updatedBy=appUserRepository.findAppUserByIdExceptSystemUser(SystemUser.ID,loggedUser.getId())
                        .orElseThrow(()->new ResourceNotFoundException("User not found"));
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
        AppUser updatedBy=appUserRepository.findAppUserByIdExceptSystemUser(SystemUser.ID,loggedUser.getId())
                .orElseThrow(()->new ResourceNotFoundException("User not found"));
        seller.setUpdatedAt(Instant.now());
        seller.setUpdatedBy(updatedBy);
        seller.setActive(true);
    }

    private SellerOutputDto entityToDto(Seller seller){
        AppUserAuditDto createdBy=AppUserAuditDto.appUserToAuditAppUserDto(seller.getCreatedBy());
        AppUserAuditDto updatedBy=AppUserAuditDto.appUserToAuditAppUserDto(seller.getUpdatedBy());

        return new SellerOutputDto(seller.getId(),seller.getCreatedAt(),createdBy,seller.getUpdatedAt(),updatedBy,seller.isActive(),
                seller.getFullName(),seller.getCpf(),seller.getEmail(), seller.getPhone());
    }

    private Seller getSellerAccordingMethodOrThrow(Supplier<Optional<Seller>> supplier){
        return supplier.get().orElseThrow(()->new ResourceNotFoundException("Seller not found"));
    }
}
