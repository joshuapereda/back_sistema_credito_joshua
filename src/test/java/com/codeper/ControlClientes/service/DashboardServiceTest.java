package com.codeper.ControlClientes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import com.codeper.ControlClientes.dto.DashboardResumenDto;
import com.codeper.ControlClientes.dto.ProductoVendidoDto;
import com.codeper.ControlClientes.entity.EstadoCompra;
import com.codeper.ControlClientes.repository.ClienteRepository;
import com.codeper.ControlClientes.repository.CompraRepository;
import com.codeper.ControlClientes.repository.DetalleCompraRepository;
import com.codeper.ControlClientes.repository.PagoRepository;

// Prueba el service con repositorios simulados (no usa la BD)
@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock private CompraRepository compraRepository;
    @Mock private PagoRepository pagoRepository;
    @Mock private ClienteRepository clienteRepository;
    @Mock private DetalleCompraRepository detalleCompraRepository;
    @InjectMocks private DashboardService dashboardService;

    @Test
    void armaElResumenConLosDatosDeLosRepositorios() {
        List<ProductoVendidoDto> top = List.of(new ProductoVendidoDto(1, "Polo", 7L));
        when(compraRepository.sumarSaldoPorCobrar()).thenReturn(new BigDecimal("426.00"));
        when(compraRepository.sumarVentasEntre(any(), any())).thenReturn(new BigDecimal("457.00"));
        when(compraRepository.countByEstado(EstadoCompra.PENDIENTE)).thenReturn(4L);
        when(clienteRepository.count()).thenReturn(12L);
        when(pagoRepository.sumarPagosEntre(any(), any())).thenReturn(new BigDecimal("30.00"));
        when(detalleCompraRepository.listarMasVendidos(PageRequest.of(0, 5))).thenReturn(top);

        DashboardResumenDto r = dashboardService.obtenerResumen();

        assertEquals(new BigDecimal("426.00"), r.getSaldoPorCobrar());
        assertEquals(new BigDecimal("457.00"), r.getVentasMes());
        assertEquals(4L, r.getComprasPendientes());
        assertEquals(12L, r.getTotalClientes());
        assertEquals(new BigDecimal("30.00"), r.getCobradoHoy());
        assertEquals(top, r.getProductosMasVendidos());
    }

    @Test
    void usaElMesActualParaVentasYElDiaDeHoyParaCobros() {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioMes = hoy.withDayOfMonth(1);

        dashboardService.obtenerResumen();

        verify(compraRepository).sumarVentasEntre(
            inicioMes.atStartOfDay(), inicioMes.plusMonths(1).atStartOfDay());
        verify(pagoRepository).sumarPagosEntre(
            hoy.atStartOfDay(), hoy.plusDays(1).atStartOfDay());
    }
}
