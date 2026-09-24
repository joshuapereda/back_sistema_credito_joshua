package com.codeper.ControlClientes.dto;

import com.codeper.ControlClientes.entity.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class RolDto {

    private Integer id;
    private String nombre;
    private String descripcion;

    public static  RolDto fromEntityRol(Rol rol){
        return  RolDto.builder()
            .id(rol.getId())
            .nombre(rol.getNombre())
            .descripcion(rol.getDescripcion())
            .build();
    }
}
