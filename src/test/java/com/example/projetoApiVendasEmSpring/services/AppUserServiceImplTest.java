package com.example.projetoApiVendasEmSpring.services;

import com.example.projetoApiVendasEmSpring.dtos.appUser.AppUserInputDto;
import com.example.projetoApiVendasEmSpring.dtos.appUser.AppUserOutputDto;
import com.example.projetoApiVendasEmSpring.entities.AppUser;
import com.example.projetoApiVendasEmSpring.entities.enums.UserRole;
import com.example.projetoApiVendasEmSpring.excepetions.BusinessException;
import com.example.projetoApiVendasEmSpring.excepetions.EmailAddressAlreadyIsUseException;
import com.example.projetoApiVendasEmSpring.repositories.AppUserRepository;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppUserServiceImplTest {
    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private ArgumentCaptor<AppUser> captor;

    @Mock
    PasswordEncoder encoder;

    @InjectMocks
    private AppUserServiceImpl service;


    @Test
    @DisplayName("should Created User with successful")
    public void shouldCreatedUser(){
        //ARRANGE
        AppUserInputDto inputDto=new AppUserInputDto("testUser","test@local.com","teste", UserRole.USER);
        UserDetailsImpl loggedUser=mock(UserDetailsImpl.class);
        AppUser createdBy=new AppUser(null,"default user","defaultUser@gmail.com","12345",UserRole.ADMIN);
        AppUser appUser=new AppUser(createdBy,inputDto.fullName(),inputDto.email(),inputDto.password(),UserRole.USER);
        UUID randomId=UUID.randomUUID();
        when(loggedUser.getId()).thenReturn(randomId);
        when(appUserRepository.findAppUserByIdExceptSystemUser(SystemUser.ID,randomId)).thenReturn(Optional.of(createdBy));
        when(encoder.encode(any())).thenReturn(inputDto.password());
        when(appUserRepository.save(any())).thenReturn(appUser);

        //ACT
        AppUserOutputDto appUserOutputDto=service.createAppUser(inputDto,loggedUser);

        //ASSERT
        verify(appUserRepository).save(any());
        assertNotNull(appUserOutputDto);
    }

    @Test
    @DisplayName("should throw a error because received email to create a user already exists")
    public void shouldThrowAErrorBecauseReceiveEmailAlreadyExists(){
        //ARRANGE
        AppUserInputDto inputDto=new AppUserInputDto("testeUser","testUser@gmai.com","123",UserRole.USER);
        UserDetailsImpl loggedUser=mock(UserDetailsImpl.class);
        when(appUserRepository.verifyExistenceAppUserByEmail(eq(inputDto.email()))).thenReturn(true);

        assertThrows(EmailAddressAlreadyIsUseException.class,()->service.createAppUser(inputDto,loggedUser));
    }

}