package com.codeper.ControlClientes.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.codeper.ControlClientes.common.dto.PageableRequest;
import com.codeper.ControlClientes.common.dto.PageableResponse;
import com.codeper.ControlClientes.dto.CreatePagoDto;
import com.codeper.ControlClientes.dto.PagoDto;
import com.codeper.ControlClientes.dto.PagoFilterCriteria;
import com.codeper.ControlClientes.entity.Compra;
import com.codeper.ControlClientes.entity.EstadoCompra;
import com.codeper.ControlClientes.entity.Pago;
import com.codeper.ControlClientes.exception.BadRequestException;
import com.codeper.ControlClientes.exception.ConflictException;
import com.codeper.ControlClientes.exception.ResourNotFoundException;
import com.codeper.ControlClientes.repository.CompraRepository;
import com.codeper.ControlClientes.repository.PagoRepository;
import com.codeper.ControlClientes.repository.specofocation.PagoSpecification;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PagoService implements IPagoService {
    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Override
    public List<PagoDto> listarPago() {
        return pagoRepository.findAll().stream()
                .map(PagoDto::fromEntityPago)
                .toList();
    }

    @Override
    public PagoDto obtenerPagoPorId(Integer id) {
        return pagoRepository.findById(id)
                .map(PagoDto::fromEntityPago)
                .orElseThrow(() -> new ResourNotFoundException("Pago ", id));
    }

    @Override
    @Transactional
    public PagoDto crearPago(CreatePagoDto createPagoDto) {

        if (createPagoDto.getMonto() == null
                || createPagoDto.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("El monto debe ser mayor a cero.");
        }

        Compra compra = compraRepository.findById(createPagoDto.getCompraId())
                .orElseThrow(() -> new ResourNotFoundException("Compra ", createPagoDto.getCompraId()));

        // 1. Validar que aún se pueda pagar
        if (compra.getEstado() == EstadoCompra.PAGADO) {
            throw new ConflictException("Esta compra ya está pagada.");
        }

        // 2. Calcular y validar el nuevo saldo
        BigDecimal saldoRes = compra.getSaldo().subtract(createPagoDto.getMonto());
        if (saldoRes.compareTo(BigDecimal.ZERO) < 0) {
            throw new ConflictException("El monto del pago excede el saldo de la compra.");
        }

        // 3. Crear el pago
        Pago pago = new Pago();
        pago.setCompra(compra);
        pago.setMonto(createPagoDto.getMonto());
        pago.setFechaHora(LocalDateTime.now());
        pago.setSaldoRestante(saldoRes);

        // 4. Actualizar la compra
        compra.setSaldo(saldoRes);
        if (saldoRes.compareTo(BigDecimal.ZERO) == 0) {
            compra.setEstado(EstadoCompra.PAGADO);
        }

        pagoRepository.save(pago);
        compraRepository.save(compra);

        // 5. Devolver el DTO
        return PagoDto.fromEntityPago(pago);
    }

    @Override
    @Transactional
    public PagoDto actualizarPago(Integer id, PagoDto actualizarPagoDto) {

        // Paso 1: buscar el pago
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourNotFoundException("Pago ", id));

        // Paso 2: validar el monto
        if (actualizarPagoDto.getMonto() == null
                || actualizarPagoDto.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("El monto debe ser mayor a cero.");
        }

        // Paso 3: cambiar el monto y guardarlo ya en la BD (flush)
        pago.setMonto(actualizarPagoDto.getMonto());
        pagoRepository.saveAndFlush(pago);

        // Paso 4: recalcular saldos de todos los pagos y de la compra
        recalcularSaldos(pago.getCompra());

        // Paso 5: devolver el DTO
        return PagoDto.fromEntityPago(pago);
    }

    @Override
    @Transactional
    public void eliminarPago(Integer id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourNotFoundException("Pago ", id));

        Compra compra = pago.getCompra();

        pagoRepository.delete(pago);
        pagoRepository.flush();

        recalcularSaldos(compra);
    }

    private void recalcularSaldos(Compra compra) {
        List<Pago> pagos = pagoRepository.findByCompraIdOrderByIdAsc(compra.getId());
        BigDecimal saldo = compra.getTotal();
        for (Pago p : pagos) {
            saldo = saldo.subtract(p.getMonto());
            p.setSaldoRestante(saldo);
        }

        if (saldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new ConflictException("El monto de los pagos excede el total de la compra.");
        }

        compra.setSaldo(saldo);
        compra.setEstado(
                saldo.compareTo(BigDecimal.ZERO) == 0 ? EstadoCompra.PAGADO : EstadoCompra.PENDIENTE);
        compraRepository.save(compra);
    }

@Override
public PageableResponse<PagoDto> getPagoPagination( PageableRequest<PagoFilterCriteria> pageableRequest) {

    try {

        // SORT PERMITE MANEJAR EL ORDENAMIENTO ASC = Ascendente, DESC = Descendente
        Sort sort = "asc".equalsIgnoreCase(pageableRequest.getSortOrder())
                ? Sort.by(pageableRequest.getSortField()).ascending()
                : Sort.by(pageableRequest.getSortField()).descending();

        // CONFIGURAMOS LA PAGINACION
        Pageable pageable = PageRequest.of(pageableRequest.getPageNumber(),pageableRequest.getPageSize(),sort);

        // FILTRADO
        // Creamos la Specification para aplicar los filtros
        // filterValue = Busqueda general por nombre del cliente
        // criterials = Filtros especificos por nombre del cliente
        Specification<Pago> specification =
                PagoSpecification.createSpecification(
                        pageableRequest.getCriterials(),
                        pageableRequest.getFilterValue()
                );

        // EJECUTAMOS LA CONSULTA
        Page<Pago> pagoPage = pagoRepository.findAll(specification, pageable);

        // DEVOLVEMOS LOS RESULTADOS
        return PageableResponse.<PagoDto>builder()

                // Convertimos cada entidad Pago en PagoDto
                .content(pagoPage.getContent().stream()
                        .map(PagoDto::fromEntityPago)
                        .toList())

                // Numero de pagina actual
                .pageNumber(pagoPage.getNumber())

                // Cantidad de registros por pagina
                .pageSize(pagoPage.getSize())

                // Total de registros encontrados
                .totalElements(pagoPage.getTotalElements())

                // Cantidad total de paginas
                .totalPages(pagoPage.getTotalPages())

                .build();

    } catch (PropertyReferenceException | IllegalArgumentException e) {
            // sortField que no existe o página inválida -> 400
            throw new BadRequestException("Datos de paginación inválidos: " + e.getMessage());
    }
  }
}