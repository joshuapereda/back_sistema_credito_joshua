package com.codeper.ControlClientes.exception;

public class ConflictException extends DomainException {

    public ConflictException(String message) {
        super(message,409,"CONFLICT");
        //TODO Auto-generated constructor stub
    }

}
