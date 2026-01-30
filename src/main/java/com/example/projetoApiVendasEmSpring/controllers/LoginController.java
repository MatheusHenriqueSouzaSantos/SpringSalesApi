package com.example.projetoApiVendasEmSpring.controllers;

import com.example.projetoApiVendasEmSpring.dtos.login.JwtTokenDto;
import com.example.projetoApiVendasEmSpring.dtos.login.LoginAppUserInputDto;
import com.example.projetoApiVendasEmSpring.excepetions.ApiErrorDto;
import com.example.projetoApiVendasEmSpring.security.SecurityUtils;
import com.example.projetoApiVendasEmSpring.security.UserDetailsImpl;
import com.example.projetoApiVendasEmSpring.security.jwt.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthenticationManager authenticationManager;

    private final SecurityUtils util;

    private final JwtService jwtService;

    public LoginController(AuthenticationManager authenticationManager, SecurityUtils util, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.util = util;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<?>login(@RequestBody @Valid LoginAppUserInputDto dto){
        Authentication authentication=new UsernamePasswordAuthenticationToken(dto.email(),dto.password());

        authentication=authenticationManager.authenticate(authentication);

        UserDetailsImpl userDetails=(UserDetailsImpl) authentication.getPrincipal();
        if(!util.verifyUserIsActiveByEmail(userDetails.getEmail())){
            ApiErrorDto erro=new ApiErrorDto(HttpStatus.FORBIDDEN,"The User is inactive");

            return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(erro);
        }

        JwtTokenDto token=new JwtTokenDto(jwtService.generateJwt(userDetails));

        return ResponseEntity.ok(token);
    }
}
