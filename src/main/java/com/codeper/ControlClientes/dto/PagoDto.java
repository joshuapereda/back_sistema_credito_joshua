package com.codeper.ControlClientes.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.codeper.ControlClientes.entity.Pago;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class PagoDto {

    private Integer id;

    private BigDecimal monto;

    private LocalDateTime fechaHora;
    
    private String nombreClienteCompra;

    private BigDecimal saldoRestante;

    private BigDecimal totalCompra;

    public static PagoDto fromEntityPago(Pago pago){
        return PagoDto.builder()
                .id(pago.getId())
                .monto(pago.getMonto())
                .fechaHora(pago.getFechaHora())
                .nombreClienteCompra(pago.getCompra().getCliente().getPersona().getNombre())
                .saldoRestante(pago.getSaldoRestante())
                .totalCompra(pago.getCompra().getTotal())
                .build();
    }

}
