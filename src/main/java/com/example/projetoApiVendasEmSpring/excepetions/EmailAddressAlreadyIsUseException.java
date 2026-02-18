package com.example.projetoApiVendasEmSpring.excepetions;

import org.springframework.http.HttpStatus;

public class EmailAddressAlreadyIsUseException extends BusinessException {
    public EmailAddressAlreadyIsUseException() {
        super(HttpStatus.BAD_REQUEST,"Email address already is use");
    }
}
