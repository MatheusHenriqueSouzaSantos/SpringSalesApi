package com.example.projetoApiVendasEmSpring.services.customer;

import com.example.projetoApiVendasEmSpring.dtos.appUser.AuditAppUserDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.customerOutputDto.CorporateCustomerOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.customerOutputDto.CustomerOutPutDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.customerOutputDto.IndividualCustomerOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.address.AddressInputDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.address.AddressOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.corporateCustomer.CorporateCustomerCreateDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.corporateCustomer.CorporateCustomerUpdateDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.individualCustomer.IndividualCustomerCreateDto;
import com.example.projetoApiVendasEmSpring.dtos.customer.individualCustomer.IndividualCustomerUpdateDto;
import com.example.projetoApiVendasEmSpring.entities.*;
import com.example.projetoApiVendasEmSpring.excepetions.BusinessException;
import com.example.projetoApiVendasEmSpring.excepetions.ResourceNotFoundException;
import com.example.projetoApiVendasEmSpring.repositories.*;
import com.example.projetoApiVendasEmSpring.security.SecurityUtils;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;
import com.example.projetoApiVendasEmSpring.services.validation.DocumentValidation;
import com.example.projetoApiVendasEmSpring.services.interfaces.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final IndividualCustomerRepository individualCustomerRepository;

    private final CorporateCustomerRepository corporateCustomerRepository;

    private final AddressRepository addressRepository;

    private final AppUserRepository appUserRepository;

    private final DocumentValidation validation;

    public CustomerServiceImpl(CustomerRepository customerRepository, IndividualCustomerRepository individualCustomerRepository,
                               CorporateCustomerRepository corporateCustomerRepository, AddressRepository addressRepository,
                               AppUserRepository appUserRepository, DocumentValidation validation) {
        this.customerRepository = customerRepository;
        this.individualCustomerRepository = individualCustomerRepository;
        this.corporateCustomerRepository = corporateCustomerRepository;
        this.addressRepository = addressRepository;
        this.appUserRepository = appUserRepository;
        this.validation = validation;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CustomerOutPutDto> findAll() {
        List<Customer> customers=customerRepository.findAllByOrderByActiveDesc();
        List<CustomerOutPutDto> customersDto=new ArrayList<>();

        for (Customer customer: customers){
            if(customer instanceof IndividualCustomer){
                customersDto.add(entityToIndividualCustomerDto((IndividualCustomer) customer));
                continue;
            }
            customersDto.add(entityToCorporateCustomerDto((CorporateCustomer) customer));
        }
        return customersDto;
    }

    @Transactional(readOnly = true)
    @Override
    public CustomerOutPutDto findCustomerById(UUID id) {
        Customer customer= customerRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Customer not found"));
        if(customer instanceof IndividualCustomer){
            return entityToIndividualCustomerDto((IndividualCustomer) customer);
        }
        return entityToCorporateCustomerDto((CorporateCustomer) customer);
    }

    @Override
    public CustomerOutPutDto findActiveCustomerById(UUID id) {
        Customer customer= customerRepository.findByIdAndActiveTrue(id)
                .orElseThrow(()->new ResourceNotFoundException("Customer not found"));
        if(customer instanceof IndividualCustomer){
            return entityToIndividualCustomerDto((IndividualCustomer) customer);
        }
        return entityToCorporateCustomerDto((CorporateCustomer) customer);
    }


    @Transactional(readOnly = true)
    @Override
    public IndividualCustomerOutputDto findCustomerByCpf(String cpf) {
        if(!validation.validateCpf(cpf)){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Cpf in invalid format");
        }
        IndividualCustomer customer=individualCustomerRepository.findByCpf(cpf)
                .orElseThrow(()->new ResourceNotFoundException("Customer not found"));
        return entityToIndividualCustomerDto(customer);

    }

    @Override
    public IndividualCustomerOutputDto findActiveCustomerByCpf(String cpf) {
        if(!validation.validateCpf(cpf)){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Cpf in invalid format");
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
        CorporateCustomer customer=corporateCustomerRepository.findByCnpj(cnpj)
                .orElseThrow(()->new ResourceNotFoundException("Customer not found"));
        return entityToCorporateCustomerDto(customer);
    }

    @Override
    public CorporateCustomerOutputDto findActiveCustomerByCnpj(String cnpj) {
        if(!validation.validateCnpj(cnpj)){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Cnpj in invalid format");
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
        IndividualCustomer customerUpdate= getIndividualCustomerByIdOrThrow(id);
        if(!customerUpdate.isActive()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The User is inactive, active it first to update");
        }
        Optional<IndividualCustomer> customerOptional=individualCustomerRepository.findByEmail(dto.email());
        if(customerOptional.isPresent() && customerOptional.get().getId()!=id){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The email received is already registered");
        }
        if(corporateCustomerRepository.existsByEmail(dto.email())){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The email received is already registered");
        }
        Address addressUpdate=customerUpdate.getAddress();
        AppUser updatedBy=this.getAppUserOrThrow(loggedUser);
        addressUpdate.setUpdatedAt(Instant.now());
        addressUpdate.setUpdatedBy(updatedBy);
        addressUpdate.setStreet(dto.address().street());
        addressUpdate.setStreetNumber(dto.address().streetNumber());
        addressUpdate.setNeighborhood(dto.address().neighborhood());
        addressUpdate.setCity(dto.address().city());
        addressUpdate.setStateCode(dto.address().stateCode());

        customerUpdate.setUpdatedAt(Instant.now());
        customerUpdate.setUpdatedBy(updatedBy);
        customerUpdate.setEmail(dto.email());
        customerUpdate.setPhone(dto.phone());
        customerUpdate.setFullName(dto.fullName());

        return entityToIndividualCustomerDto(customerUpdate);
    }

    @Transactional
    @Override
    public CorporateCustomerOutputDto updatedCorporateCustomer(UUID id, CorporateCustomerUpdateDto dto, UserDetailsImpl loggedUser) {
        CorporateCustomer customerUpdate= getCorporateCustomerByIdOrThrow(id);
        if(!customerUpdate.isActive()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The User is inactive, active it first to update");
        }
        Optional<CorporateCustomer> customerOptional=corporateCustomerRepository.findByEmail(dto.email());
        if(customerOptional.isPresent() && customerOptional.get().getId()!=id){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The email received is already registered");
        }
        if(individualCustomerRepository.existsByEmail(dto.email())){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The email received is already registered");
        }

        Address addressUpdate=customerUpdate.getAddress();
        AppUser updatedBy=this.getAppUserOrThrow(loggedUser);
        addressUpdate.setUpdatedAt(Instant.now());
        addressUpdate.setUpdatedBy(updatedBy);
        addressUpdate.setStreet(dto.address().street());
        addressUpdate.setStreetNumber(dto.address().streetNumber());
        addressUpdate.setNeighborhood(dto.address().neighborhood());
        addressUpdate.setCity(dto.address().city());
        addressUpdate.setStateCode(dto.address().stateCode());

        customerUpdate.setUpdatedAt(Instant.now());
        customerUpdate.setUpdatedBy(updatedBy);
        customerUpdate.setEmail(dto.email());
        customerUpdate.setPhone(dto.phone());
        customerUpdate.setLegalName(dto.legalName());
        customerUpdate.setTradeName(dto.tradeName());
        customerUpdate.setStateRegistration(dto.stateRegistration());

        return entityToCorporateCustomerDto(customerUpdate);
    }

    //just for adms
    @Transactional
    @Override
    public void deActivateCustomer(UUID id, UserDetailsImpl loggedUser) {
        Customer customer = getIndividualOrCorporateCustomerByIdOrThrow(id);

        if(!customer.isActive()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The customer is already inactive");
        }

        customer.setUpdatedAt(Instant.now());
        AppUser updatedBy=getAppUserOrThrow(loggedUser);
        customer.setUpdatedBy(updatedBy);
        customer.setActive(false);
    }
    @Transactional
    @Override
    public void reActivateCustomer(UUID id, UserDetailsImpl loggedUser) {
        Customer customer = getIndividualOrCorporateCustomerByIdOrThrow(id);

        if(customer.isActive()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The customer is already active");
        }

        customer.setUpdatedAt(Instant.now());
        AppUser updatedBy=getAppUserOrThrow(loggedUser);
        customer.setUpdatedBy(updatedBy);
        customer.setActive(true);
    }

    private IndividualCustomerOutputDto entityToIndividualCustomerDto(IndividualCustomer customer){
        AuditAppUserDto createdBy= AuditAppUserDto.appUserToAuditAppUserDto(customer.getCreatedBy());
        AuditAppUserDto updatedBy= AuditAppUserDto.appUserToAuditAppUserDto(customer.getUpdatedBy());

        AddressOutputDto addressDto=AddressOutputDto.addressEntityToAddressDto(customer.getAddress());

        return new IndividualCustomerOutputDto(customer.getId(),customer.getCreatedAt(),createdBy,customer.getUpdatedAt(),updatedBy,
                customer.isActive(),customer.getEmail(), customer.getPhone(),addressDto, customer.getFullName(), customer.getCpf());
    }

    private CorporateCustomerOutputDto entityToCorporateCustomerDto(CorporateCustomer customer){
        AuditAppUserDto createdBy= AuditAppUserDto.appUserToAuditAppUserDto(customer.getCreatedBy());
        AuditAppUserDto updatedBy= AuditAppUserDto.appUserToAuditAppUserDto(customer.getUpdatedBy());

        AddressOutputDto addressDto=AddressOutputDto.addressEntityToAddressDto(customer.getAddress());

        return new CorporateCustomerOutputDto(customer.getId(),customer.getCreatedAt(),createdBy,customer.getUpdatedAt(),updatedBy,
                customer.isActive(),customer.getEmail(), customer.getPhone(),addressDto, customer.getLegalName(), customer.getTradeName(),
                customer.getStateRegistration(), customer.getCnpj());
    }

    private IndividualCustomer getIndividualCustomerByIdOrThrow(UUID id){
        return individualCustomerRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Individual customer not found"));
    }

    private CorporateCustomer getCorporateCustomerByIdOrThrow(UUID id){
        return corporateCustomerRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Corporate customer not found"));
    }

    private Customer getIndividualOrCorporateCustomerByIdOrThrow(UUID id){
        Optional<IndividualCustomer> individualCustomer=individualCustomerRepository.findById(id);
        if(individualCustomer.isPresent()){
            return individualCustomer.get();
        }
        Optional<CorporateCustomer> corporateCustomer=corporateCustomerRepository.findById(id);
        if(corporateCustomer.isPresent()){
            return corporateCustomer.get();
        }
        throw new ResourceNotFoundException("Customer not found");
    }


    private AppUser getAppUserOrThrow(UserDetailsImpl loggedUser){
        return appUserRepository.findByIdAndActiveTrue(loggedUser.getId()).
                orElseThrow(()->new ResourceNotFoundException("User not found"));
    }
}
