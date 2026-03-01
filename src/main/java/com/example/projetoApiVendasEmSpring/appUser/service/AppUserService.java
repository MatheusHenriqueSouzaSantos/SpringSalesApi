package com.example.projetoApiVendasEmSpring.appUser.service;

import com.example.projetoApiVendasEmSpring.appUser.dto.AppUserInputDto;
import com.example.projetoApiVendasEmSpring.appUser.dto.AppUserOutputDto;
import com.example.projetoApiVendasEmSpring.security.userDetails.UserDetailsImpl;

import java.util.List;
import java.util.UUID;

public interface AppUserService {
    List<AppUserOutputDto> findAllAppUsers();

    AppUserOutputDto findAppUserById(UUID id);

    AppUserOutputDto getAppUserMe(UserDetailsImpl user);

    AppUserOutputDto findAppUserByEmail(String email);

    AppUserOutputDto createAppUser(AppUserInputDto dto,UserDetailsImpl loggedUser);

    AppUserOutputDto updateAppUser(UUID userId,AppUserInputDto dto,UserDetailsImpl loggedUser);

    void deActivateAppUserById(UUID id, UserDetailsImpl loggedUser);

    void reActivateAppUserById(UUID id, UserDetailsImpl loggedUser);
}
