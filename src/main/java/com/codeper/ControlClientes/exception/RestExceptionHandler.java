package com.codeper.ControlClientes.exception;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.codeper.ControlClientes.common.CustomErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

//Indica que este va ser un manejador global de errores 
@RestControllerAdvice 
public class RestExceptionHandler {
    
    //Si queremos manejar estos tipos de errores, debemos definir su manejador
    //Definir manejador de la plantilla
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<CustomErrorResponse<String>> handleDomainException(DomainException x, HttpServletRequest req){
        return new ResponseEntity<>(CustomErrorResponse.<String>builder()
        .statusCode(x.getStatusCode())
        .errorCode(x.getErrorCode())
        .message(x.getMessage())
        .timestamp(new Date().toString())
        .path(req.getRequestURI())
        .build(), new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    //Definir metodos 
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<CustomErrorResponse<String>> handleException(Exception x, HttpServletRequest req){
        return new ResponseEntity<>(CustomErrorResponse.<String>builder()
        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
        .message(x.getMessage())
        .timestamp(new Date().toString())
        .path(req.getRequestURI())
        .build(), new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Manejar error de Validacion
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CustomErrorResponse<List<String>>> handleValidationException(MethodArgumentNotValidException x, HttpServletRequest req){
        //variable auxiliar
        List<String>errors=x.getBindingResult().getFieldErrors().stream()
            .map(error ->error.getField()+": "+error.getDefaultMessage())
            .collect(Collectors.toList());
        return new ResponseEntity<>(CustomErrorResponse.<List<String>>builder()
        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
        .message(errors)
        .timestamp(new Date().toString())
        .path(req.getRequestURI())
        .build(), new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
