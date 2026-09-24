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
    @NotBlank (message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank (message = "La foto no puede estar vacía")
    private String foto;

    @NotNull(message = "El precio de venta no puede estar vacío")
    @DecimalMin(value = "0.01", message = "El precio de venta debe ser mayor a 0")
    private BigDecimal precioVenta;
    
    @NotNull(message = "El precio de compra no puede estar vacío")
    @DecimalMin(value = "0.01", message = "El precio de compra debe ser mayor a 0")
    private BigDecimal precioCompra;

    @NotBlank (message = "El nombre de la categoría no puede estar vacío")
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
