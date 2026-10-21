package com.codeper.ControlClientes.dto;

import com.codeper.ControlClientes.entity.Permiso;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class CreatePermisoDto {

    @NotBlank(message = "El nombre del permiso es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @NotBlank(message = "El recurso es obligatorio")
    @Size(max = 50, message = "El recurso no puede superar los 50 caracteres")
    @Pattern (regexp = "^[A-Za-z_]+$",
             message = "El recurso solo puede contener letras y guion bajo")
    private String recurso;

    @NotBlank(message = "La acción es obligatoria")
    @Pattern(regexp = "^(?i)(CREAR|LEER|ACTUALIZAR|ELIMINAR)$",
             message = "La acción debe ser CREAR, LEER, ACTUALIZAR o ELIMINAR")
    private String accion;

    public static Permiso toEntityPermisos(CreatePermisoDto dto) {
        Permiso permiso = new Permiso();
        permiso.setNombre(dto.getNombre().trim());
        permiso.setRecurso(dto.getRecurso().trim().toUpperCase());
        permiso.setAccion(dto.getAccion().trim().toUpperCase());
        return permiso;
    }
}
