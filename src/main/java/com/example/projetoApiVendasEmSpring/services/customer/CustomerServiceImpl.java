package com.example.projetoApiVendasEmSpring.services.customer;

import com.example.projetoApiVendasEmSpring.dtos.appUser.AppUserAuditDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.CorporateCustomerOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.CustomerOutPutDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.IndividualCustomerOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.address.AddressInputDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.address.AddressOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.corporateCustomer.CorporateCustomerCreateDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.corporateCustomer.CorporateCustomerUpdateDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.individualCustomer.IndividualCustomerCreateDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.individualCustomer.IndividualCustomerUpdateDto;
import com.example.projetoApiVendasEmSpring.entities.Address;
import com.example.projetoApiVendasEmSpring.entities.AppUser;
import com.example.projetoApiVendasEmSpring.entities.CorporateCustomer;
import com.example.projetoApiVendasEmSpring.entities.IndividualCustomer;
import com.example.projetoApiVendasEmSpring.excepetions.BusinessException;
import com.example.projetoApiVendasEmSpring.excepetions.ResourceNotFoundException;
import com.example.projetoApiVendasEmSpring.repositories.AddressRepository;
import com.example.projetoApiVendasEmSpring.repositories.AppUserRepository;
import com.example.projetoApiVendasEmSpring.repositories.CorporateCustomerRepository;
import com.example.projetoApiVendasEmSpring.repositories.IndividualCustomerRepository;
import com.example.projetoApiVendasEmSpring.security.SecurityUtils;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;
import com.example.projetoApiVendasEmSpring.services.interfaces.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final IndividualCustomerRepository individualCustomerRepository;

    private final CorporateCustomerRepository corporateCustomerRepository;

    private final AddressRepository addressRepository;

    private final AppUserRepository appUserRepository;

    private final CustomerValidation validation;

    private final SecurityUtils util;

    public CustomerServiceImpl(IndividualCustomerRepository individualCustomerRepository, CorporateCustomerRepository corporateCustomerRepository,
                               AddressRepository addressRepository, AppUserRepository appUserRepository, CustomerValidation validation,
                               SecurityUtils util) {
        this.individualCustomerRepository = individualCustomerRepository;
        this.corporateCustomerRepository = corporateCustomerRepository;
        this.addressRepository = addressRepository;
        this.appUserRepository = appUserRepository;
        this.validation = validation;
        this.util = util;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CustomerOutPutDto> findAll() {
        if(util.isAdmin()){
            List<IndividualCustomer> individualCustomers=individualCustomerRepository.findAllOrderByActiveDesc();
            List<CorporateCustomer> corporateCustomers=corporateCustomerRepository.findAllOrderByActiveDesc();

            List<CustomerOutPutDto> customersDto=new ArrayList<>();

            customersDto.addAll(individualCustomers.stream().map(this::entityToIndividualCustomerDto).toList());
            customersDto.addAll(corporateCustomers.stream().map(this::entityToCorporateCustomerDto).toList());

            return customersDto;
        }
        List<IndividualCustomer> individualCustomers=individualCustomerRepository.findByActiveTrue();
        List<CorporateCustomer> corporateCustomers = corporateCustomerRepository.findByActiveTrue();

        List<CustomerOutPutDto> customersDto=new ArrayList<>();

        customersDto.addAll(individualCustomers.stream().map(this::entityToIndividualCustomerDto).toList());
        customersDto.addAll(corporateCustomers.stream().map(this::entityToCorporateCustomerDto).toList());

        return customersDto;

    }

    @Transactional(readOnly = true)
    @Override
    public CustomerOutPutDto findCustomerById(UUID id) {
        return findCustomerByUserTypeOrThrow(id);
    }

    @Transactional(readOnly = true)
    @Override
    public IndividualCustomerOutputDto findCustomerByCpf(String cpf) {
        if(!validation.validateCpf(cpf)){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Cpf in invalid format");
        }
        if(util.isAdmin()){
             IndividualCustomer customer=individualCustomerRepository.findByCpf(cpf)
                    .orElseThrow(()->new ResourceNotFoundException("Customer not found"));
             return entityToIndividualCustomerDto(customer);
        }
        IndividualCustomer customer=individualCustomerRepository.findByCpfAndActiveTrue(cpf)
                .orElseThrow(()->new ResourceNotFoundException("Customer not found"));
        return entityToIndividualCustomerDto(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public CorporateCustomerOutputDto findCustomerByCnpj(String cnpj) {
        if(!validation.validateCnpj(cnpj)){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Cnpj in invalid format");
        }
        if(util.isAdmin()){
            CorporateCustomer customer=corporateCustomerRepository.findByCnpj(cnpj)
                    .orElseThrow(()->new ResourceNotFoundException("Customer not found"));
            return entityToCorporateCustomerDto(customer);
        }
        CorporateCustomer customer=corporateCustomerRepository.findByCnpjAndActiveTrue(cnpj)
                .orElseThrow(()->new ResourceNotFoundException("Customer not found"));
        return entityToCorporateCustomerDto(customer);
    }

    @Transactional
    @Override
    public IndividualCustomerOutputDto createIndividualCustomer(IndividualCustomerCreateDto dto, UserDetailsImpl loggedUser) {
        if(!validation.validateCpf(dto.cpf())){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Cpf in invalid format");
        }
        if(individualCustomerRepository.existsByCpf(dto.cpf())){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The cpf received is already registered");
        }
        if(individualCustomerRepository.existsByEmail(dto.email()) || corporateCustomerRepository.existsByEmail(dto.email())){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The email received is already registered");
        }
        AppUser createBy=getAppUserOrThrow(loggedUser);
        IndividualCustomer customer=new IndividualCustomer(createBy,dto.email(), dto.phone(), dto.fullName(), dto.cpf());
        AddressInputDto addressDto=dto.address();
        Address address=new Address(createBy,addressDto.street(),addressDto.streetNumber(),addressDto.neighborhood(),
                addressDto.city(),addressDto.stateCode(),customer);
        customer.setAddress(address);
        individualCustomerRepository.save(customer);
        addressRepository.save(address);

        return entityToIndividualCustomerDto(customer);
    }

    @Transactional
    @Override
    public CorporateCustomerOutputDto createCorporateCustomer(CorporateCustomerCreateDto dto, UserDetailsImpl loggedUser) {
        if(!validation.validateCnpj(dto.cnpj())){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Cnpj in invalid format");
        }
        if(corporateCustomerRepository.existsByCnpj(dto.cnpj())){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The Cnpj received is already registered");
        }
        if(corporateCustomerRepository.existsByEmail(dto.email()) || individualCustomerRepository.existsByEmail(dto.email())){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The email received is already registered");
        }
        AppUser createBy=getAppUserOrThrow(loggedUser);
        CorporateCustomer customer=new CorporateCustomer(createBy,dto.email(), dto.phone(),dto.legalName(),dto.tradeName(),dto.stateRegistration(),
                dto.cnpj());
        AddressInputDto addressDto=dto.address();
        Address address=new Address(createBy,addressDto.street(),addressDto.streetNumber(),addressDto.neighborhood(),
                addressDto.city(),addressDto.stateCode(),customer);
        customer.setAddress(address);
        corporateCustomerRepository.save(customer);
        addressRepository.save(address);

        return entityToCorporateCustomerDto(customer);
    }

    @Transactional
    @Override
    public IndividualCustomerOutputDto updateIndividualCustomer(UUID id,IndividualCustomerUpdateDto dto, UserDetailsImpl loggedUser) {
        Optional<IndividualCustomer> customerOptional=individualCustomerRepository.findByEmail(dto.email());
        if(customerOptional.isPresent() && customerOptional.get().getId()!=id){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The email received is already registered");
        }
        IndividualCustomer customerUpdate=individualCustomerRepository.findByIdAndActiveTrue(id)
                .orElseThrow(()->new ResourceNotFoundException("Individual customer not found"));
        Address addressUpdate=customerUpdate.getAddress();

    }

    @Transactional
    @Override
    public CorporateCustomerOutputDto updatedCorporateCustomer(CorporateCustomerUpdateDto dto, UserDetailsImpl loggedUser) {
        return null;
    }

    @Transactional
    @Override
    public void deActivateCustomer(UUID id, UserDetailsImpl loggedUser) {

    }
    @Transactional
    @Override
    public void reActivateCustomer(UUID id, UserDetailsImpl loggedUser) {

    }

    private IndividualCustomerOutputDto entityToIndividualCustomerDto(IndividualCustomer customer){
        AppUserAuditDto createdBy=AppUserAuditDto.appUserToAuditAppUserDto(customer.getCreatedBy());
        AppUserAuditDto updatedBy=AppUserAuditDto.appUserToAuditAppUserDto(customer.getUpdatedBy());

        AddressOutputDto addressDto=AddressOutputDto.addressEntityToAddressDto(customer.getAddress());

        return new IndividualCustomerOutputDto(customer.getId(),customer.getCreatedAt(),createdBy,customer.getUpdatedAt(),updatedBy,
                customer.isActive(),customer.getEmail(), customer.getPhone(),addressDto, customer.getFullName(), customer.getCpf());
    }

    private CorporateCustomerOutputDto entityToCorporateCustomerDto(CorporateCustomer customer){
        AppUserAuditDto createdBy=AppUserAuditDto.appUserToAuditAppUserDto(customer.getCreatedBy());
        AppUserAuditDto updatedBy=AppUserAuditDto.appUserToAuditAppUserDto(customer.getUpdatedBy());

        AddressOutputDto addressDto=AddressOutputDto.addressEntityToAddressDto(customer.getAddress());

        return new CorporateCustomerOutputDto(customer.getId(),customer.getCreatedAt(),createdBy,customer.getUpdatedAt(),updatedBy,
                customer.isActive(),customer.getEmail(), customer.getPhone(),addressDto, customer.getLegalName(), customer.getTradeName(),
                customer.getStateRegistration(), customer.getCnpj());
    }

//    private IndividualCustomer getIndividualCustomerActiveByIdOrThrow(UUID id){
//        return individualCustomerRepository.findByIdAndActiveTrue(id)
//                .orElseThrow(()->new ResourceNotFoundException("Individual customer not found"));
//    }
//
//    private CorporateCustomer getCorporateCustomerActiveByIdOrThrow(UUID id){
//        return corporateCustomerRepository.findByIdAndActiveTrue(id)
//                .orElseThrow(()->new ResourceNotFoundException("Corporate customer not found"));
//    }
//
//    private IndividualCustomer getIndividualCustomerByIdOrThrow(UUID id){
//        return individualCustomerRepository.findById(id)
//                .orElseThrow(()->new ResourceNotFoundException("Individual customer not found"));
//    }
//
//    private CorporateCustomer getCorporateCustomerByIdOrThrow(UUID id){
//        return corporateCustomerRepository.findById(id)
//                .orElseThrow(()->new ResourceNotFoundException("Corporate customer not found"));
//    }
    public CustomerOutPutDto findCustomerByUserTypeOrThrow(UUID id){
        if(util.isAdmin()){
            return getAllCustomerByIdOrThrow(id);
        }
        return getActiveCustomerByIdOrThrow(id);
    }

    public CustomerOutPutDto getActiveCustomerByIdOrThrow(UUID id){
        Optional<IndividualCustomer> individualCustomer=individualCustomerRepository.findByIdAndActiveTrue(id);
        if(individualCustomer.isPresent()){
            return entityToIndividualCustomerDto(individualCustomer.get());
        }
        Optional<CorporateCustomer> corporateCustomer=corporateCustomerRepository.findByIdAndActiveTrue(id);
        if(corporateCustomer.isPresent()){
            return entityToCorporateCustomerDto(corporateCustomer.get());
        }
        throw new ResourceNotFoundException("Customer not found");
    }



    public CustomerOutPutDto getAllCustomerByIdOrThrow(UUID id){
        Optional<IndividualCustomer> individualCustomer=individualCustomerRepository.findById(id);
        if(individualCustomer.isPresent()){
            return entityToIndividualCustomerDto(individualCustomer.get());
        }
        Optional<CorporateCustomer> corporateCustomer=corporateCustomerRepository.findById(id);
        if(corporateCustomer.isPresent()){
            return entityToCorporateCustomerDto(corporateCustomer.get());
        }
        throw new ResourceNotFoundException("Customer not found");
    }

    public AppUser getAppUserOrThrow(UserDetailsImpl loggedUser){
        return appUserRepository.findByIdAndActiveTrue(loggedUser.getId()).
                orElseThrow(()->new ResourceNotFoundException("User not found"));
    }
}
