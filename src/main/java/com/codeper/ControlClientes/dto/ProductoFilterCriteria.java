package com.codeper.ControlClientes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class ProductoFilterCriteria {
    //Definir criterios de busqueda
    //definir campos por lo que se va buscar
    private String nombre;
    private String descripcion;
    private String nombreCategoria;


}
