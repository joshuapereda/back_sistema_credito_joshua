package com.codeper.ControlClientes.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Todo lo que muestra el dashboard, en una sola respuesta
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardResumenDto {
    private BigDecimal saldoPorCobrar;
    private BigDecimal ventasMes;
    private Long comprasPendientes;
    private Long totalClientes;
    private BigDecimal cobradoHoy;
    private List<ProductoVendidoDto> productosMasVendidos;
}
