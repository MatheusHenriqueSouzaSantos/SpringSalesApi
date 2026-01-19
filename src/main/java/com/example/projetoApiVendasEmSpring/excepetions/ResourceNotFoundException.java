package com.example.projetoApiVendasEmSpring.excepetions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Resource not found");
    }
    public ResourceNotFoundException(String message){
        super(HttpStatus.NOT_FOUND,message);
    }
}
