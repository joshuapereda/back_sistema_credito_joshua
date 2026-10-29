package com.codeper.ControlClientes.dto;

import java.math.BigDecimal;
import com.codeper.ControlClientes.entity.Compra;
import com.codeper.ControlClientes.entity.Pago;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class CreatePagoDto {
    
    @NotNull (message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El monto admite hasta 10 enteros y 2 decimales")
    private BigDecimal monto;

    @NotNull(message = "La compra es obligatoria")
    @Positive(message = "El id de la compra debe ser un número positivo")
    private Integer compraId;

    public static Pago toEntityPago(CreatePagoDto dto, Compra compra) {
        return Pago.builder()
                .monto(dto.getMonto())
                .compra(compra)
                .build();
    }

}
