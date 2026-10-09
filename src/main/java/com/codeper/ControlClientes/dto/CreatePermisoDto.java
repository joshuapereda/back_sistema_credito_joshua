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
  
    @NotBlank (message = "El Nombre del Permiso es obligatorio")
    private String nombre;

    @NotBlank (message = "El Nombre del  recurso es obligatorio")
    private String recurso;

    @NotBlank (message = "El Nombre de la  accion es obligatorio")
    private String accion;

    public static Permiso toEntityPermisos(CreatePermisoDto createPermisoDto){
        Permiso permiso= new Permiso();
        permiso.setNombre(createPermisoDto.getNombre());
        permiso.setRecurso(createPermisoDto.getRecurso());
        permiso.setAccion(createPermisoDto.getAccion());
        return permiso;
    }

}
