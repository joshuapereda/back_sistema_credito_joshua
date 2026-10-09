package com.codeper.ControlClientes.dto;

import java.time.LocalDateTime;
import com.codeper.ControlClientes.entity.Cliente;
import com.codeper.ControlClientes.entity.Compra;
import com.codeper.ControlClientes.entity.TipoCompra;
import com.codeper.ControlClientes.entity.Usuario;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class CreateCompraDto {

    @NotBlank (message = "La Fecha y Hora es obligatorio")
    private LocalDateTime fechaHora;

    @NotBlank (message = "El Total es obligatorio")
    private Double total;

    @NotBlank (message = "El Saldo es obligatorio")
    private Double saldo;

    // RELACIONES (por ID)
    @NotBlank (message = "Debes asignar al menos un Cliente a la Compra")
    private Integer clienteId;

    @NotBlank (message = "Debes asignar al menos un Usuario a la Compra")
    private Integer usuarioId;

    @NotBlank (message = "Debes asignar al menos un Tipo de Compra a la Compra")
    private Integer tipoCompraId;

    public static Compra toEntityCompra(CreateCompraDto createCompraDto, Cliente cliente,Usuario usuario, TipoCompra tipoCompra){
        return Compra.builder()
            .fechaHora(createCompraDto.getFechaHora())
            .total(createCompraDto.getTotal())
            .saldo(createCompraDto.getSaldo())
            .cliente(cliente)
            .usuario(usuario)
            .tipoCompra(tipoCompra)
            .build();
    }
}
