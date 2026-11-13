package com.codeper.ControlClientes.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class PagoFilterCriteria {
    private String estadoCompra;
    private String nombreCliente;

}
