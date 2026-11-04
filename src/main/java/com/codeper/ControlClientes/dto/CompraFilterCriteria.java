package com.codeper.ControlClientes.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class CompraFilterCriteria {
    private String tipoCompra;
    private String nombreCliente;
    private String estadoCompra; 

}
