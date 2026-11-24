package com.codeper.ControlClientes.service;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.codeper.ControlClientes.dto.DashboardResumenDto;
import com.codeper.ControlClientes.entity.EstadoCompra;
import com.codeper.ControlClientes.repository.ClienteRepository;
import com.codeper.ControlClientes.repository.CompraRepository;
import com.codeper.ControlClientes.repository.DetalleCompraRepository;
import com.codeper.ControlClientes.repository.PagoRepository;

@Service
public class DashboardService implements IDashboardService {

    // Cuántos productos muestra el ranking
    private static final int TOP_PRODUCTOS = 5;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    @Override
    @Transactional(readOnly = true) // solo lee, no guarda nada
    public DashboardResumenDto obtenerResumen() {
        // Rangos de fecha: el final no se incluye
        LocalDate hoy = LocalDate.now();
        LocalDate inicioMes = hoy.withDayOfMonth(1);

        return DashboardResumenDto.builder()
            .saldoPorCobrar(compraRepository.sumarSaldoPorCobrar())
            .ventasMes(compraRepository.sumarVentasEntre(
                inicioMes.atStartOfDay(), inicioMes.plusMonths(1).atStartOfDay()))
            .comprasPendientes(compraRepository.countByEstado(EstadoCompra.PENDIENTE))
            .totalClientes(clienteRepository.count())
            .cobradoHoy(pagoRepository.sumarPagosEntre(
                hoy.atStartOfDay(), hoy.plusDays(1).atStartOfDay()))
            .productosMasVendidos(detalleCompraRepository.listarMasVendidos(
                PageRequest.of(0, TOP_PRODUCTOS)))
            .build();
    }
}
