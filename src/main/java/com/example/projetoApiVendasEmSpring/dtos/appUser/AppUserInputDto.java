package com.example.projetoApiVendasEmSpring.dtos.appUser;

import com.example.projetoApiVendasEmSpring.entities.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

//ter um para update???
public record AppUserInputDto(
        @NotBlank(message = "full name can not be blank")
        @Size(min = 1,max=150, message = "full name can not be less than 1 and greater 150 character")
        String fullName,
        @Email(message = "email in invalid format ")
        @NotBlank(message = "email can not be blank")
        String email,
        @NotBlank(message = "password can not be blank")
        String password,
        @NotNull(message = "role can not be null")
        UserRole role
        ){
}
