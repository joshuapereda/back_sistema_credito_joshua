package com.codeper.ControlClientes.dto;

import java.time.LocalDateTime;
import com.codeper.ControlClientes.entity.Compra;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class CompraDto {

    private Integer id;
    private LocalDateTime fechaHora;
    private Double total;
    private Double saldo;
    //RELAIONES
    private String cliente;
    private String usuario;
    private String tipoCompra;

    public static CompraDto fromEntityCompra(Compra compra) {
    return CompraDto.builder()
            .id(compra.getId())
            .fechaHora(compra.getFechaHora())
            .total(compra.getTotal())
            .saldo(compra.getSaldo())
            .cliente(compra.getCliente().getPersona().getNombre())
            .usuario(compra.getUsuario().getPersona().getNombre())
            .tipoCompra(compra.getTipoCompra().getNombre())
            .build();
}
}
