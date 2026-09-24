package com.codeper.ControlClientes.dto;

import java.math.BigDecimal;
import com.codeper.ControlClientes.entity.Producto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor
@Builder 
public class ProductoDto {
    private Integer id;
    private String nombre;
    private String foto;
    private BigDecimal precioVenta;
    private BigDecimal precioCompra;
    private String nombreCategoria;


    public static ProductoDto fromEntityProducto(Producto pproducto){
        return ProductoDto.builder()
            .id(pproducto.getId())
            .nombre(pproducto.getNombre())
            .foto(pproducto.getFoto())
            .precioVenta(pproducto.getPrecioVenta())
            .precioCompra(pproducto.getPrecioCompra())
            .nombreCategoria(pproducto.getCategoria().getNombre())
            .build();
    }

}
