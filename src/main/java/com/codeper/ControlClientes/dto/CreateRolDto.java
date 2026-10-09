package com.codeper.ControlClientes.dto;

import java.util.List;
import com.codeper.ControlClientes.entity.Permiso;
import com.codeper.ControlClientes.entity.Rol;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class CreateRolDto {
    
    @NotBlank (message = "El Nombre del rol es obligatorio")
    private String nombre;
    
    @Column(length = 200)
    @Size(max = 200, message = "La Descripción no puede superar los 200 caracteres")
    @Pattern (regexp = "^[a-zA-ZÀ-ÿñÑ0-9 .,()\\-]{0,200}$", message = "La Descripción contiene caracteres no permitidos")
    private String descripcion;

    @NotEmpty(message = "Debe asignar al menos un Permiso al Rol")
    private List<Integer>permisosId;
    

    public static Rol toEntityRol(CreateRolDto createRolDto,List<Permiso> permisos){
       return Rol.builder()
            .nombre(createRolDto.getNombre())
            .descripcion(createRolDto.getDescripcion())
            .permisos(permisos)
            .build();
    }

}
