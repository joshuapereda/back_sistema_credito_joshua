package com.codeper.ControlClientes.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.codeper.ControlClientes.entity.Compra;
import com.codeper.ControlClientes.entity.EstadoCompra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompraDto {

    // Cabecera: se muestra una sola vez
    private Integer id;
    private LocalDateTime fechaHora;
    private BigDecimal total;
    private BigDecimal saldo;

    // Relaciones: en la respuesta van como texto (nombre), no como objetos
    private String cliente;
    private String usuario;
    private String tipoCompra;

    // Líneas de la compra: una lista, un elemento por producto
    private List<DetalleDto> detalles;
    private EstadoCompra estado;  

    // Molde de UNA línea (equivale a DetalleItemDto, pero de salida)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetalleDto {
        private Integer productoId;
        private String nombreProducto;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
        
    }

    // Convierte Entidad -> DTO (se llama una vez por cada compra)
    public static CompraDto fromEntityCompra(Compra compra) {
        return CompraDto.builder()
                .id(compra.getId())
                .fechaHora(compra.getFechaHora())
                .total(compra.getTotal())
                .saldo(compra.getSaldo())
                .cliente(compra.getCliente().getPersona().getNombre())
                .usuario(compra.getUsuario().getUsername())
                .tipoCompra(compra.getTipoCompra().getNombre())
                .estado(compra.getEstado()) 
                // stream sobre la LISTA de detalles: cada DetalleCompra -> un DetalleDto
                .detalles(compra.getDetalles().stream()
                        .map(d -> new DetalleDto(
                                d.getProducto().getId(),
                                d.getProducto().getNombre(),
                                d.getCantidad(),
                                d.getPrecioUnitario(),
                                d.getSubtotal()))
                        .toList())
                .build();
    }
}