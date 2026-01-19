package com.example.projetoApiVendasEmSpring.excepetions;

import org.springframework.http.HttpStatus;

public record ApiErrorDto(HttpStatus status, String message) {
}
