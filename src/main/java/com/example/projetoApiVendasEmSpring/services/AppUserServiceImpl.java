package com.example.projetoApiVendasEmSpring.services;

import com.example.projetoApiVendasEmSpring.excepetions.BusinessException;
import com.example.projetoApiVendasEmSpring.excepetions.ResourceNotFoundException;
import com.example.projetoApiVendasEmSpring.dtos.appUser.AppUserInputDto;
import com.example.projetoApiVendasEmSpring.dtos.appUser.AppUserOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.appUser.AuditAppUserDto;
import com.example.projetoApiVendasEmSpring.entities.AppUser;
import com.example.projetoApiVendasEmSpring.repositories.AppUserRepository;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;
import com.example.projetoApiVendasEmSpring.services.interfaces.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository repository;
    private final PasswordEncoder encoder;

    public AppUserServiceImpl(AppUserRepository repository,PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder=encoder;
    }
    @Transactional(readOnly = true)
    public List<AppUserOutputDto> findAllAppUsers(){
        List<AppUser> users=repository.findAllAppUserExceptSystemUser(SystemUser.ID);
        List<AppUserOutputDto> dtoUsers = new ArrayList<>();
        for (AppUser user : users){
            dtoUsers.add(entityToDto(user));
        }
        return dtoUsers;
    }
    @Transactional(readOnly = true)
    public AppUserOutputDto findAppUserById(UUID id) {
        Optional<AppUser> user=repository.findAppUserByIdExceptSystemUser(SystemUser.ID,id);
        if(user.isEmpty()){
            throw new ResourceNotFoundException();
        }
        return entityToDto(user.get());
    }
    @Transactional(readOnly = true)
    @Override
    public AppUserOutputDto getAppUserMe(UserDetailsImpl userFromRequest) {
        AppUser user=repository.findAppUserByIdExceptSystemUser(SystemUser.ID,userFromRequest.getId())
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));;
        return entityToDto(user);
    }
    @Transactional(readOnly = true)
    public AppUserOutputDto findAppUserByEmail(String email) {
        AppUser user=repository.findAppUserByEmailExceptSystemUser(SystemUser.ID,email)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));
        return entityToDto(user);
    }
    @Transactional
    public AppUserOutputDto createAppUser(AppUserInputDto dto, UserDetailsImpl loggedUser) {
        if(repository.verifyExistenceAppUserByEmail(dto.email())){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"already exists a user with this email");
        }
        AppUser createBy=repository.findAppUserByIdExceptSystemUser(SystemUser.ID,loggedUser.getId()).get();
        String passwordHash=encoder.encode(dto.password());
        AppUser createdUser=new AppUser(createBy,dto.fullName(),dto.email(),passwordHash,dto.role());
        return entityToDto(repository.save(createdUser));
    }
    //do not need to save because object is tracked
    @Transactional
    public AppUserOutputDto updateAppUser(UUID userId,AppUserInputDto dto,UserDetailsImpl loggedUser) {
        AppUser user=repository.findAppUserByIdExceptSystemUser(SystemUser.ID,userId)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));

        if(repository.verifyExistenceAppUserByEmail(dto.email())){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"already exists a user with this email");
        }
        AppUser updatedBy=repository.findAppUserByIdExceptSystemUser(SystemUser.ID,loggedUser.getId()).get();
        user.setUpdatedAt(Instant.now());
        user.setUpdatedBy(updatedBy);
        user.setFullName(dto.fullName());
        user.setEmail(dto.email());
        user.setPasswordHash(encoder.encode(dto.password()));

        return entityToDto(user);
    }
    @Transactional
    public void deActivateAppUserById(UUID id, UserDetailsImpl loggedUser) {
        AppUser user=repository.findAppUserByIdExceptSystemUser(SystemUser.ID,id)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));

        if(!user.isActive()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The User is already inactivated");
        }

        AppUser updatedBy=repository.findAppUserByIdExceptSystemUser(SystemUser.ID,loggedUser.getId()).get();

        user.setUpdatedBy(updatedBy);
        user.setUpdatedAt(Instant.now());
        user.setActive(false);
    }
    @Transactional
    public void reActivateAppUserById(UUID id, UserDetailsImpl loggedUser){
        AppUser user=repository.findAppUserByIdExceptSystemUser(SystemUser.ID,id)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));

        if(user.isActive()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"The User is already active");
        }

        AppUser updatedBy=repository.findAppUserByIdExceptSystemUser(SystemUser.ID,loggedUser.getId()).get();

        user.setUpdatedAt(Instant.now());
        user.setUpdatedBy(updatedBy);
        user.setActive(true);
    }

    private AppUserOutputDto entityToDto(AppUser user){
        AuditAppUserDto createdBy=AuditAppUserDto.appUserToAuditAppUserDto(user.getCreatedBy());
        AuditAppUserDto updatedBy= AuditAppUserDto.appUserToAuditAppUserDto(user.getUpdatedBy());

        return new AppUserOutputDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                createdBy,
                updatedBy,
                user.getFullName(),
                user.getEmail(),
                user.getUserRole(),
                user.isActive()
        );
    }

}
