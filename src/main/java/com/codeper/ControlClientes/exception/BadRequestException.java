package com.codeper.ControlClientes.exception;

// 400: los datos enviados están mal
public class BadRequestException extends DomainException {

    public BadRequestException(String message) {
        super(message, 400, "BAD_REQUEST");
    }
}
