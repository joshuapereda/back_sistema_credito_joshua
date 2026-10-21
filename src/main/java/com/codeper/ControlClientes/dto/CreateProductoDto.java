package com.codeper.ControlClientes.dto;

import java.math.BigDecimal;
import com.codeper.ControlClientes.entity.Categoria;
import com.codeper.ControlClientes.entity.Producto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class CreateProductoDto {

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @Size(max = 255, message = "La ruta de la foto no puede superar los 255 caracteres")
    private String foto;

    @NotNull(message = "El precio de venta es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio de venta debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El precio de venta admite hasta 8 enteros y 2 decimales")
    private BigDecimal precioVenta;

    @NotNull(message = "El precio de compra es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio de compra debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El precio de compra admite hasta 8 enteros y 2 decimales")
    private BigDecimal precioCompra;

    @NotNull(message = "Debes asignar una categoría al producto")
    @Positive(message = "El id de la categoría no es válido")
    private Integer categoriaId;

    public static Producto toEntityPProducto(CreateProductoDto dto, Categoria categoria) {
        return Producto.builder()
                .nombre(dto.getNombre().trim())
                .foto(dto.getFoto())
                .precioVenta(dto.getPrecioVenta())
                .precioCompra(dto.getPrecioCompra())
                .categoria(categoria)
                .build();
    }
}
