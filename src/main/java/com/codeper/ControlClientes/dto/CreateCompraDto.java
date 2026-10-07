package com.codeper.ControlClientes.dto;

import java.time.LocalDateTime;

import com.codeper.ControlClientes.entity.Cliente;
import com.codeper.ControlClientes.entity.Compra;
import com.codeper.ControlClientes.entity.TipoCompra;
import com.codeper.ControlClientes.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class CreateCompraDto {

    private LocalDateTime fechaHora;
    private Double total;
    private Double saldo;
    // RELACIONES (por ID)
    private Integer clienteId;
    private Integer usuarioId;
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
