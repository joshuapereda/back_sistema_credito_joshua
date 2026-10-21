package com.codeper.ControlClientes.dto;

import com.codeper.ControlClientes.entity.TipoCompra;
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
public class CreateTipoCompraDto {

    @NotBlank(message = "El nombre del tipo de compra es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
    @Pattern (regexp = "^[A-Za-zÀ-ÿñÑ ]*$",
             message = "El nombre solo puede contener letras y espacios")
    private String nombre;

    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
    private String descripcion;

    public static TipoCompra toEntityTipoCompra(CreateTipoCompraDto dto) {
        TipoCompra tipoCompra = new TipoCompra();
        tipoCompra.setNombre(dto.getNombre().trim());
        tipoCompra.setDescripcion(dto.getDescripcion() != null ? dto.getDescripcion().trim() : null);
        return tipoCompra;
    }
}
