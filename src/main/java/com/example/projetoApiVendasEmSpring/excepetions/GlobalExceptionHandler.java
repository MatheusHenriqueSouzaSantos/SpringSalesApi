package com.example.projetoApiVendasEmSpring.excepetions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorDto> handleBusiness(BusinessException ex){
        return ResponseEntity.status(ex.getStatus()).body(new ApiErrorDto(ex.getStatus().value(),ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDto> handleValidation(MethodArgumentNotValidException ex){
        String message=ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage() )
                .toList()
                .toString();

        return ResponseEntity.badRequest().body(new ApiErrorDto(HttpStatus.BAD_REQUEST.value(),message));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorDto> handleTypeMissMatch(MethodArgumentTypeMismatchException ex){
        String message= "one or more arguments are invalid";
        return ResponseEntity.badRequest().body(new ApiErrorDto(HttpStatus.BAD_REQUEST.value(),message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDto> handleException(Exception ex){
        return ResponseEntity.internalServerError().body(new ApiErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage()));
    }
}
