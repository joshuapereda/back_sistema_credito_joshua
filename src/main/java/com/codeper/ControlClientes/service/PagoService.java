package com.codeper.ControlClientes.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codeper.ControlClientes.dto.CreatePagoDto;
import com.codeper.ControlClientes.dto.PagoDto;
import com.codeper.ControlClientes.entity.Compra;
import com.codeper.ControlClientes.entity.EstadoCompra;
import com.codeper.ControlClientes.entity.Pago;
import com.codeper.ControlClientes.repository.CompraRepository;
import com.codeper.ControlClientes.repository.PagoRepository;
import org.springframework.transaction.annotation.Transactional;

@Service 
public class PagoService  implements IPagoService {
    @Autowired 
    private PagoRepository pagoRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Override
    public List<PagoDto> listarPago() {
        return  pagoRepository.findAll().stream()
                .map(PagoDto:: fromEntityPago)
                .toList();
    }

    @Override
    public PagoDto obtenerPagoPorId(Integer id) {
        return pagoRepository.findById(id)
        .map(PagoDto::fromEntityPago)
        .orElseThrow(()-> new RuntimeException("Pago no encontrada con id: " + id));
    }

    @Override   
    @Transactional 
    public PagoDto crearPago(CreatePagoDto createPagoDto) {

    if (createPagoDto.getMonto() == null
            || createPagoDto.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
        throw new RuntimeException("El monto debe ser mayor a cero.");
    }

    Compra compra = compraRepository.findById(createPagoDto.getCompraId())
        .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

    // 1. Validar que aún se pueda pagar
    if (compra.getEstado() == EstadoCompra.PAGADO) {
        throw new RuntimeException("Esta compra ya está pagada.");
    }

    // 2. Calcular y validar el nuevo saldo
    BigDecimal saldoRes = compra.getSaldo().subtract(createPagoDto.getMonto());
    if (saldoRes.compareTo(BigDecimal.ZERO) < 0) {
        throw new RuntimeException("El monto del pago excede el saldo de la compra.");
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
    
    // 5. Devolver el DTO  // <- esta línea es la que te falta
    return PagoDto.fromEntityPago(pago);
    }

    @Override
    @Transactional
    public PagoDto actualizarPago(Integer id, PagoDto actualizarPagoDto) {

        Compra compra= compraRepository.findById(actualizarPagoDto.getId())
            .orElseThrow(()-> new RuntimeException("Compra no encontrada con id: " + actualizarPagoDto.getId()));

        Pago pagoRetrieved = pagoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pago no encontrado con id: " + id));
        
        pagoRetrieved.setMonto(actualizarPagoDto.getMonto());
        pagoRetrieved.setSaldoRestante(actualizarPagoDto.getSaldoRestante());
        pagoRetrieved.setFechaHora(LocalDateTime.now());
        pagoRetrieved.setCompra(compra);

        pagoRepository.save(pagoRetrieved);
        return PagoDto.fromEntityPago(pagoRetrieved);
    }

    @Override
    public void eliminarPago(Integer id) {
        if (!pagoRepository.existsById(id)) {
        throw new RuntimeException("Pago no encontrado con id: " + id);
        }
        pagoRepository.deleteById(id);
    }

}
