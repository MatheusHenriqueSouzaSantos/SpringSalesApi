package com.example.projetoApiVendasEmSpring.services.appUser;

import com.example.projetoApiVendasEmSpring.excepetions.BusinessException;
import com.example.projetoApiVendasEmSpring.excepetions.EmailAddressAlreadyIsUseException;
import com.example.projetoApiVendasEmSpring.excepetions.ResourceNotFoundException;
import com.example.projetoApiVendasEmSpring.dtos.appUser.AppUserInputDto;
import com.example.projetoApiVendasEmSpring.dtos.appUser.AppUserOutputDto;
import com.example.projetoApiVendasEmSpring.dtos.appUser.AuditAppUserDto;
import com.example.projetoApiVendasEmSpring.entities.AppUser;
import com.example.projetoApiVendasEmSpring.repositories.AppUserRepository;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;
import com.example.projetoApiVendasEmSpring.services.SystemUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

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
            throw new EmailAddressAlreadyIsUseException();
        }
        AppUser createBy=getActiveAppUserByIdOrThrow(loggedUser.getId());
        String passwordHash=encoder.encode(dto.password());
        AppUser createdUser=new AppUser(createBy,dto.fullName(),dto.email(),passwordHash,dto.role());
        return entityToDto(repository.save(createdUser));
    }
    @Transactional
    public AppUserOutputDto updateAppUser(UUID userId,AppUserInputDto dto,UserDetailsImpl loggedUser) {
        AppUser user= getActiveAppUserByIdOrThrow(userId);
        Optional<AppUser> existingUserWithReceiveEmail =repository.findAppUserByEmailExceptSystemUser(SystemUser.ID, dto.email());

        if(existingUserWithReceiveEmail.isPresent() && existingUserWithReceiveEmail.get().getId()!= userId){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"already exists a user with this email");
        }
        AppUser updatedBy= getActiveAppUserByIdOrThrow(loggedUser.getId());
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

        AppUser updatedBy=getActiveAppUserByIdOrThrow(loggedUser.getId());

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

        AppUser updatedBy=getActiveAppUserByIdOrThrow(loggedUser.getId());

        user.setUpdatedAt(Instant.now());
        user.setUpdatedBy(updatedBy);
        user.setActive(true);
    }

    private AppUserOutputDto entityToDto(AppUser user){
        AuditAppUserDto createdBy= AuditAppUserDto.appUserToAuditAppUserDto(user.getCreatedBy());
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
    private AppUser getActiveAppUserByIdOrThrow(UUID id){
        AppUser appUser= repository.findAppUserByIdExceptSystemUser(SystemUser.ID,id).
                orElseThrow(()->new ResourceNotFoundException("User not found"));
        if(!appUser.isActive()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"App user is inactive and can not do any action");
        }
        return appUser;
    }

}
