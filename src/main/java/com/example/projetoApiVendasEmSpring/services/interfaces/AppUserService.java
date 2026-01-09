package com.example.projetoApiVendasEmSpring.services.interfaces;

import com.example.projetoApiVendasEmSpring.dtos.AppUserInputDto;
import com.example.projetoApiVendasEmSpring.entities.AppUser;

import java.util.List;
import java.util.UUID;

public interface AppUserService {
    List<AppUser> getAllAppUsers();

    AppUser getAppUserById(UUID id);

    AppUser creatAppUser(AppUserInputDto dto);

    AppUser updateAppUser(AppUserInputDto dto);

    void deleteAppUserById(UUID id);
}
