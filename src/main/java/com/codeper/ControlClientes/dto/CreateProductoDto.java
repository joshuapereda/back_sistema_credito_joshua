package com.codeper.ControlClientes.dto;

import java.math.BigDecimal;
import com.codeper.ControlClientes.entity.Categoria;
import com.codeper.ControlClientes.entity.Producto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class CreateProductoDto {

    @NotBlank (message = "El Nombre del producto es obligatorio")
    private String nombre;

    
    private String foto;

    @NotNull(message = "El Precio de venta es obligatorio")
    @DecimalMin(value = "0.01", message = "El Precio de venta debe ser mayor a 0")
    private BigDecimal precioVenta;
    
    @NotNull(message = "El Precio de compra es obligatorio")
    @DecimalMin(value = "0.01", message = "El Precio de compra debe ser mayor a 0")
    private BigDecimal precioCompra;

    @NotBlank (message = "Debes asignar al menos una categotia al Producto")
    private String nombreCategoria;

    public static Producto toEntityPProducto(CreateProductoDto createPProductoDto, Categoria pcategoria){
        return Producto.builder()
            .nombre(createPProductoDto.getNombre())
            .foto(createPProductoDto.getFoto())
            .precioVenta(createPProductoDto.getPrecioVenta())
            .precioCompra(createPProductoDto.getPrecioCompra())
            .categoria(pcategoria)
            .build();
    }
}
