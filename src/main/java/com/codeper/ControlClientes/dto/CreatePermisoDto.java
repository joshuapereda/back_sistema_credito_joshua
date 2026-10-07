package com.codeper.ControlClientes.dto;

import com.codeper.ControlClientes.entity.Permiso;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class CreatePermisoDto {
  
    @NotBlank (message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank (message = "El recurso no puede estar vacío")
    private String recurso;

    @NotBlank (message = "El accion no puede estar vacío")
    private String accion;

    public static Permiso toEntityPermisos(CreatePermisoDto createPermisoDto){
        Permiso permiso= new Permiso();
        permiso.setNombre(createPermisoDto.getNombre());
        permiso.setRecurso(createPermisoDto.getRecurso());
        permiso.setAccion(createPermisoDto.getAccion());
        return permiso;
    }

}
