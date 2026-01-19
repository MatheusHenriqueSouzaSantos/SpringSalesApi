package com.example.projetoApiVendasEmSpring.excepetions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {
    @Setter
    private HttpStatus status;

    public BusinessException(HttpStatus status,String message){
        super(message);
        this.status=status;
    }
}
