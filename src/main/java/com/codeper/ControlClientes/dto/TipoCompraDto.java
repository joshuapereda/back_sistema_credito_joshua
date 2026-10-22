package com.codeper.ControlClientes.dto;

import com.codeper.ControlClientes.entity.TipoCompra;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class TipoCompraDto {
    private Integer id;
    private String nombre;
    private String descripcion;
    private Boolean esCredito;

    public static TipoCompraDto fromEntityTipoCompra( TipoCompra tipoCompra){
        return TipoCompraDto.builder()
            .id(tipoCompra.getId())
            .nombre(tipoCompra.getNombre())
            .descripcion(tipoCompra.getDescripcion())
            .esCredito(tipoCompra.getEsCredito())
            .build();
    }
}
