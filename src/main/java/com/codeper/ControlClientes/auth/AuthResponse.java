package com.codeper.ControlClientes.auth;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data 
@AllArgsConstructor 
@Builder 
public class AuthResponse {

    private String accesToken;
    private String refreshToken;
    private Integer identifier;
    private Long expiration;

}
