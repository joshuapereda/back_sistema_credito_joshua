package com.codeper.ControlClientes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Una fila del ranking de productos más vendidos
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoVendidoDto {
    private Integer productoId;
    private String nombre;
    private Long cantidad; // SUM devuelve Long
}
