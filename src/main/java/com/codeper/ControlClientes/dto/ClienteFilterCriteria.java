package com.codeper.ControlClientes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class ClienteFilterCriteria {
    private String docIdentidad;
    private String nombrePersona;
}
