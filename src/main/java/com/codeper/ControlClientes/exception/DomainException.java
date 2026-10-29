package com.codeper.ControlClientes.exception;

import lombok.Getter;

/*Una clase abstract no necesita ser instanciada y lo utilizamos 
como plantilla, para definir en base a esta otras clases */
@Getter 
public abstract class DomainException extends RuntimeException {
    //--------ERRORES PERSONALIZADOS-----------//
  //Definimos lo atributos  
  private int statusCode; // aca va el nuemro del error 400
  private String errorCode;// Bad Request

  //Definicion
  public DomainException(String message,int statusCode,String errorCode ){
    //el message hereda del RuntimeException
        super(message);
        this.statusCode=statusCode;
        this.errorCode=errorCode;
  }

}
