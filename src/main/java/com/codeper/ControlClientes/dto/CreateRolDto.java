package com.codeper.ControlClientes.dto;

import com.codeper.ControlClientes.entity.Rol;
import jakarta.persistence.Column;
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
public class CreateRolDto {
    
    @NotBlank (message = "El nombre no puede estar vacío")
    private String nombre;
    
    @Column(length = 200)
    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
    @Pattern (regexp = "^[a-zA-ZÀ-ÿñÑ0-9 .,()\\-]{0,200}$", message = "La descripción contiene caracteres no permitidos")
    private String descripcion;

    public static Rol toEntityRol(CreateRolDto createRolDto){
        Rol rol =new Rol();
        rol.setNombre(createRolDto.getNombre());
        rol.setDescripcion(createRolDto.getDescripcion());
        return  rol;
    }

}
