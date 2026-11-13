package com.codeper.ControlClientes.dto;

import com.codeper.ControlClientes.entity.Categoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class CategoriaDto {
    
    private Integer id;
    private String nombre;
    private String  foto;
    private String descripcion;

    public static CategoriaDto fromEntityPCategoria( Categoria pCategoria){
        return CategoriaDto.builder()
            .id(pCategoria.getId())
            .nombre(pCategoria.getNombre())
            .foto(pCategoria.getFoto())
            .descripcion(pCategoria.getDescripcion())
            .build();
    }
}
