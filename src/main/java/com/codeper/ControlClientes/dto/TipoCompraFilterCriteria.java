package com.codeper.ControlClientes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class TipoCompraFilterCriteria {
    private String nombre;
    private String descripcion;
}
