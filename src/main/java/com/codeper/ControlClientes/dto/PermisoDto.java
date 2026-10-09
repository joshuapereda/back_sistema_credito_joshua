package com.codeper.ControlClientes.dto;

import com.codeper.ControlClientes.entity.Permiso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class PermisoDto {
    private Integer id;
    private String nombre;
    private String recurso;
    private String accion;

    public static PermisoDto fromEntityPermiso(Permiso permiso){
       return PermisoDto.builder()
       .id(permiso.getId())
       .nombre(permiso.getNombre())
       .recurso(permiso.getRecurso())
       .accion(permiso.getAccion())
       .build();
    }

}
