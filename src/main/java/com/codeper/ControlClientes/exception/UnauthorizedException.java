package com.codeper.ControlClientes.exception;

// 401: no inició sesión o su token no sirve
public class UnauthorizedException extends DomainException {

    public UnauthorizedException(String message) {
        super(message, 401, "UNAUTHORIZED");
    }
}
