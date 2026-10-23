package com.codeper.ControlClientes.exception;

public class ResourNotFoundException extends DomainException {

    public ResourNotFoundException( String resource, Object id) { //con el Object indicamos que nuetsra id puede tener cualqueir tipo de dato
        super(resource+" con id"+id+" no encontrado", 404,"RESOURDCE_NOT_FOUND");
        //TODO Auto-generated constructor stub
    }

}
