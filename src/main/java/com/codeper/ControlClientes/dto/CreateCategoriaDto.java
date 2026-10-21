package com.codeper.ControlClientes.dto;

import com.codeper.ControlClientes.entity.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class CreateCategoriaDto {

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
    private String nombre;

    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
    private String descripcion;

    public static Categoria toEntityCategoria(CreateCategoriaDto dto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre() != null ? dto.getNombre().trim() : null);
        categoria.setDescripcion(dto.getDescripcion() != null ? dto.getDescripcion().trim() : null);
        return categoria;
    }
}
