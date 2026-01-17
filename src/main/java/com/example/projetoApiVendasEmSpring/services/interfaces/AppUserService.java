package com.example.projetoApiVendasEmSpring.services.interfaces;

import com.example.projetoApiVendasEmSpring.dtos.appUser.AppUserInputDto;
import com.example.projetoApiVendasEmSpring.dtos.appUser.AppUserOutputDto;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;

import java.util.List;
import java.util.UUID;

public interface AppUserService {
    List<AppUserOutputDto> getAllAppUsers();

    AppUserOutputDto getAppUserById(UUID id);

    AppUserOutputDto getAppUserMe(UserDetailsImpl user);

    AppUserOutputDto getAppUserByEmail(String email);

    AppUserOutputDto createAppUser(AppUserInputDto dto,UserDetailsImpl loggedUser);

    AppUserOutputDto updateAppUser(UUID userId,AppUserInputDto dto,UserDetailsImpl loggedUser);

    void deActivateAppUserById(UUID id, UserDetailsImpl loggedUser);
}
