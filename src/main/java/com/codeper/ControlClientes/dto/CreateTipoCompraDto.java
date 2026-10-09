package com.codeper.ControlClientes.dto;

import com.codeper.ControlClientes.entity.TipoCompra;
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
public class CreateTipoCompraDto {

    @NotBlank (message = "El Nombre del Tipo de Compra es obligatorio")
    @Size(min=1,max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String nombre;
    
    @Size(max = 200, message = "La Descripción no puede tener más de 200 caracteres")
    private String descripcion;

    public static TipoCompra toEntityTipoCompra(CreateTipoCompraDto createTipoCompraDto){
        TipoCompra tcompraCompra=new TipoCompra();
        tcompraCompra.setNombre(createTipoCompraDto.getNombre());
        tcompraCompra.setDescripcion(createTipoCompraDto.getDescripcion());
        return  tcompraCompra;
    }

}
