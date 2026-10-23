package com.codeper.ControlClientes.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class CustomErrorResponse<T> {
    //En el mensaje agregamos como tipo de dato una T por 
    //que eso indica que ese atributo puede tener muchos tipos de
    //datos como Int,string, double etc es un tipo de dato generico

    private int statusCode; // aca va el nuemro del error 400
    private String errorCode;// Bad Request
    private String timestamp; //fecha
    private T message; //mensaje de error 
    private String path; //ruta del error 


}
